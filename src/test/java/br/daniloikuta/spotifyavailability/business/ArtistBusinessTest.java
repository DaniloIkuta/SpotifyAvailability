package br.daniloikuta.spotifyavailability.business;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import br.daniloikuta.spotifyavailability.dto.ArtistDto;
import br.daniloikuta.spotifyavailability.entity.ArtistEntity;
import br.daniloikuta.spotifyavailability.repository.ArtistRepository;

@ExtendWith(MockitoExtension.class)
public class ArtistBusinessTest {
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
}
