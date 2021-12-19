package br.daniloikuta.spotifyavailability.business;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.persistence.EntityNotFoundException;

import org.hibernate.envers.DefaultRevisionEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.envers.repository.support.DefaultRevisionMetadata;
import org.springframework.data.history.Revision;
import org.springframework.data.history.RevisionMetadata.RevisionType;
import org.springframework.data.history.Revisions;

import br.daniloikuta.spotifyavailability.dto.ArtistDto;
import br.daniloikuta.spotifyavailability.dto.RevisionDto;
import br.daniloikuta.spotifyavailability.dto.RevisionMetadataDto;
import br.daniloikuta.spotifyavailability.entity.ArtistEntity;
import br.daniloikuta.spotifyavailability.repository.ArtistRepository;

@ExtendWith(MockitoExtension.class)
class ArtistBusinessTest {
	@InjectMocks
	private ArtistBusiness artistBusiness;

	@Mock
	private ArtistRepository artistRepository;

	@Test
	void testFindArtistsNoneFound () {
		final List<String> ids = Arrays.asList("artistId1", "artistId2");

		when(artistRepository.findAllById(ids)).thenReturn(new ArrayList<>());

		final List<ArtistDto> artists = artistBusiness.findArtists(ids);

		assertTrue(artists.isEmpty());
	}

	@Test
	void testFindArtists () {
		final List<String> ids = Arrays.asList("artistId1", "artistId2");

		when(artistRepository.findAllById(ids))
			.thenReturn(Arrays.asList(ArtistEntity.builder().id("artistId1").name("artist1").build(),
				ArtistEntity.builder().id("artistId2").name("artist2").build()));

		final List<ArtistDto> artists = artistBusiness.findArtists(ids);

		final List<ArtistDto> expected = Arrays.asList(ArtistDto.builder().id("artistId1").name("artist1").build(),
			ArtistDto.builder().id("artistId2").name("artist2").build());
		assertEquals(expected, artists);
	}

	@Test
	void testGetRevisionHistoryNotFound () {
		when(artistRepository.findRevisions("id")).thenReturn(Revisions.none());
		assertThrows(EntityNotFoundException.class, () -> artistBusiness.getRevisionHistory("id"));
	}

	@Test
	void testGetRevisionHistory () {
		setupTestGetRevisionHistory();

		final List<RevisionDto<ArtistDto>> revisionHistory = artistBusiness.getRevisionHistory("id");

		final RevisionDto<ArtistDto> revision1 = RevisionDto.<ArtistDto>builder()
			.entry(ArtistDto.builder().id("id").build())
			.metadata(RevisionMetadataDto.builder()
				.id(1)
				.timestamp(LocalDateTime.ofInstant(Instant.ofEpochMilli(1638652499L), ZoneId.systemDefault()))
				.type("INSERT")
				.build())
			.build();
		final RevisionDto<ArtistDto> revision2 = RevisionDto.<ArtistDto>builder()
			.entry(ArtistDto.builder().id("id").name("artist").build())
			.metadata(RevisionMetadataDto.builder()
				.id(2)
				.timestamp(LocalDateTime.ofInstant(Instant.ofEpochMilli(1638652500L), ZoneId.systemDefault()))
				.type("UPDATE")
				.build())
			.build();
		final RevisionDto<ArtistDto> revision3 = RevisionDto.<ArtistDto>builder()
			.metadata(RevisionMetadataDto.builder()
				.id(3)
				.timestamp(LocalDateTime.ofInstant(Instant.ofEpochMilli(1638652501L), ZoneId.systemDefault()))
				.type("DELETE")
				.build())
			.build();
		final List<RevisionDto<ArtistDto>> expected = Arrays.asList(revision1, revision2, revision3);

		assertEquals(expected, revisionHistory);
	}

	private void setupTestGetRevisionHistory () {
		final DefaultRevisionEntity revisionEntity1 = new DefaultRevisionEntity();
		revisionEntity1.setId(1);
		revisionEntity1.setTimestamp(1638652499L);

		final DefaultRevisionEntity revisionEntity2 = new DefaultRevisionEntity();
		revisionEntity2.setId(2);
		revisionEntity2.setTimestamp(1638652500L);

		final DefaultRevisionEntity revisionEntity3 = new DefaultRevisionEntity();
		revisionEntity3.setId(3);
		revisionEntity3.setTimestamp(1638652501L);

		final List<Revision<Integer, ArtistEntity>> revisions = Arrays.asList(
			Revision.of(new DefaultRevisionMetadata(revisionEntity1, RevisionType.INSERT),
				ArtistEntity.builder().id("id").build()),
			Revision.of(new DefaultRevisionMetadata(revisionEntity2, RevisionType.UPDATE),
				ArtistEntity.builder().id("id").name("artist").build()),
			Revision.of(new DefaultRevisionMetadata(revisionEntity3, RevisionType.DELETE),
				null));

		when(artistRepository.findRevisions("id")).thenReturn(Revisions.of(revisions));
	}
}
