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
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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

import com.neovisionaries.i18n.CountryCode;

import br.daniloikuta.spotifyavailability.dto.AlbumDto;
import br.daniloikuta.spotifyavailability.dto.ArtistDto;
import br.daniloikuta.spotifyavailability.dto.RevisionDto;
import br.daniloikuta.spotifyavailability.dto.RevisionMetadataDto;
import br.daniloikuta.spotifyavailability.dto.TrackDto;
import br.daniloikuta.spotifyavailability.entity.AlbumEntity;
import br.daniloikuta.spotifyavailability.entity.ArtistEntity;
import br.daniloikuta.spotifyavailability.entity.GenreEntity;
import br.daniloikuta.spotifyavailability.entity.MarketEntity;
import br.daniloikuta.spotifyavailability.entity.TrackEntity;
import br.daniloikuta.spotifyavailability.enums.AlbumType;
import br.daniloikuta.spotifyavailability.enums.ReleaseDatePrecision;
import br.daniloikuta.spotifyavailability.repository.AlbumRepository;

@ExtendWith(MockitoExtension.class)
public class AlbumBusinessTest {
	@InjectMocks
	private AlbumBusiness albumBusiness;

	@Mock
	private AlbumRepository albumRepository;

	@Test
	void testFindAlbumsNoneFound () {
		final List<String> ids = Arrays.asList("albumId1", "albumId2");

		when(albumRepository.findAllById(ids)).thenReturn(new ArrayList<>());

		final List<AlbumDto> albums = albumBusiness.findAlbums(ids);

		assertTrue(albums.isEmpty());
	}

	@Test
	void testFindAlbums () {
		final List<String> ids = Arrays.asList("albumId1", "albumId2");

		setupTestFindAlbums(ids);

		final List<AlbumDto> albums = albumBusiness.findAlbums(ids);

		final Set<ArtistDto> artists =
			new HashSet<>(Arrays.asList(ArtistDto.builder().id("artistId").name("artistName").build()));
		final Set<CountryCode> markets = new HashSet<>(Arrays.asList(CountryCode.BR, CountryCode.US));
		final Set<String> genres = new HashSet<>(Arrays.asList("genre1", "genre2"));
		final AlbumDto album1 = AlbumDto.builder()
			.artists(artists)
			.availableMarkets(markets)
			.copyrights("copyrights")
			.genres(genres)
			.id("albumId1")
			.name("album1")
			.releaseDate("2021-01-01")
			.releaseDatePrecision(ReleaseDatePrecision.DAY)
			.trackCount(1)
			.type(AlbumType.ALBUM)
			.tracks(new HashSet<>(
				Arrays.asList(TrackDto.builder().id("trackId1").name("track1Album1").artists(artists).build())))
			.build();
		final AlbumDto album2 = AlbumDto.builder()
			.artists(artists)
			.availableMarkets(markets)
			.copyrights("copyrights")
			.genres(genres)
			.id("albumId2")
			.name("album2")
			.releaseDate("2021-01-02")
			.releaseDatePrecision(ReleaseDatePrecision.DAY)
			.trackCount(2)
			.type(AlbumType.ALBUM)
			.tracks(new HashSet<>(
				Arrays.asList(TrackDto.builder().id("trackId2").name("track1Album2").artists(artists).build(),
					TrackDto.builder().id("trackId3").name("track2Album2").artists(artists).build())))
			.build();
		final List<AlbumDto> expected = Arrays.asList(album1, album2);
		assertEquals(expected, albums);
	}

	private void setupTestFindAlbums (final List<String> albumIds) {
		final Set<MarketEntity> marketEntities =
			new HashSet<>(Arrays.asList(MarketEntity.builder().code(CountryCode.BR).build(),
				MarketEntity.builder().code(CountryCode.US).build()));
		final Set<ArtistEntity> artists =
			new HashSet<>(Arrays.asList(ArtistEntity.builder().id("artistId").name("artistName").build()));
		final Set<GenreEntity> genres = new HashSet<>(Arrays.asList(GenreEntity.builder().genre("genre1").build(),
			GenreEntity.builder().genre("genre2").build()));

		final TrackEntity album1Track1 =
			TrackEntity.builder().id("trackId1").name("track1Album1").artists(artists).build();
		final TrackEntity album2Track1 =
			TrackEntity.builder().id("trackId2").name("track1Album2").artists(artists).build();
		final TrackEntity album2Track2 =
			TrackEntity.builder().id("trackId3").name("track2Album2").artists(artists).build();

		final AlbumEntity album1 = AlbumEntity.builder()
			.id("albumId1")
			.name("album1")
			.type(AlbumType.ALBUM)
			.availableMarkets(marketEntities)
			.copyrights("copyrights")
			.trackCount(1)
			.releaseDate("2021-01-01")
			.releaseDatePrecision(ReleaseDatePrecision.DAY)
			.genres(genres)
			.artists(artists)
			.tracks(new HashSet<>(Arrays.asList(album1Track1)))
			.build();

		final AlbumEntity album2 = AlbumEntity.builder()
			.id("albumId2")
			.name("album2")
			.type(AlbumType.ALBUM)
			.availableMarkets(marketEntities)
			.copyrights("copyrights")
			.trackCount(2)
			.releaseDate("2021-01-02")
			.releaseDatePrecision(ReleaseDatePrecision.DAY)
			.genres(genres)
			.artists(artists)
			.tracks(new HashSet<>(Arrays.asList(album2Track1, album2Track2)))
			.build();

		when(albumRepository.findAllById(albumIds)).thenReturn(Arrays.asList(album1, album2));
	}

	@Test
	void testGetRevisionHistoryNotFound () {
		when(albumRepository.findRevisions("id")).thenReturn(Revisions.none());
		assertThrows(EntityNotFoundException.class, () -> albumBusiness.getRevisionHistory("id"));
	}

	@Test
	void testGetRevisionHistory () {
		setupTestGetRevisionHistory();

		final List<RevisionDto<AlbumDto>> revisionHistory = albumBusiness.getRevisionHistory("id");

		final RevisionDto<AlbumDto> revision1 = RevisionDto.<AlbumDto>builder()
			.entry(AlbumDto.builder().id("id").build())
			.metadata(RevisionMetadataDto.builder()
				.id(1)
				.timestamp(LocalDateTime.ofInstant(Instant.ofEpochMilli(1638652499L), ZoneId.systemDefault()))
				.type("INSERT")
				.build())
			.build();
		final RevisionDto<AlbumDto> revision2 = RevisionDto.<AlbumDto>builder()
			.entry(AlbumDto.builder().id("id").name("album").build())
			.metadata(RevisionMetadataDto.builder()
				.id(2)
				.timestamp(LocalDateTime.ofInstant(Instant.ofEpochMilli(1638652500L), ZoneId.systemDefault()))
				.type("UPDATE")
				.build())
			.build();
		final RevisionDto<AlbumDto> revision3 = RevisionDto.<AlbumDto>builder()
			.metadata(RevisionMetadataDto.builder()
				.id(3)
				.timestamp(LocalDateTime.ofInstant(Instant.ofEpochMilli(1638652501L), ZoneId.systemDefault()))
				.type("DELETE")
				.build())
			.build();
		final List<RevisionDto<AlbumDto>> expected = Arrays.asList(revision1, revision2, revision3);

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

		final List<Revision<Integer, AlbumEntity>> revisions = Arrays.asList(
			Revision.of(new DefaultRevisionMetadata(revisionEntity1, RevisionType.INSERT),
				AlbumEntity.builder().id("id").build()),
			Revision.of(new DefaultRevisionMetadata(revisionEntity2, RevisionType.UPDATE),
				AlbumEntity.builder().id("id").name("album").build()),
			Revision.of(new DefaultRevisionMetadata(revisionEntity3, RevisionType.DELETE),
				null));

		when(albumRepository.findRevisions("id")).thenReturn(Revisions.of(revisions));
	}
}
