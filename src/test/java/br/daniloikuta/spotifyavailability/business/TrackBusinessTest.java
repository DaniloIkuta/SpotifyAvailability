package br.daniloikuta.spotifyavailability.business;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.neovisionaries.i18n.CountryCode;

import br.daniloikuta.spotifyavailability.dto.AlbumDto;
import br.daniloikuta.spotifyavailability.dto.ArtistDto;
import br.daniloikuta.spotifyavailability.dto.TrackDto;
import br.daniloikuta.spotifyavailability.entity.AlbumEntity;
import br.daniloikuta.spotifyavailability.entity.ArtistEntity;
import br.daniloikuta.spotifyavailability.entity.MarketEntity;
import br.daniloikuta.spotifyavailability.entity.TrackEntity;
import br.daniloikuta.spotifyavailability.repository.TrackRepository;

@ExtendWith(MockitoExtension.class)
public class TrackBusinessTest {
	@InjectMocks
	private TrackBusiness trackBusiness;

	@Mock
	private TrackRepository trackRepository;

	@Test
	void testFindTracksNoneFound () {
		final List<String> ids = Arrays.asList("trackId1", "trackId2");

		when(trackRepository.findAllById(ids)).thenReturn(new ArrayList<>());

		final List<TrackDto> tracks = trackBusiness.findTracks(ids);

		assertTrue(tracks.isEmpty());
	}

	@Test
	void testFindTracks () {
		final List<String> ids = Arrays.asList("trackId1", "trackId2");

		setupTestFindTracks(ids);

		final List<TrackDto> tracks = trackBusiness.findTracks(ids);

		final ArtistDto artist1Dto = getArtist1Dto();
		final Set<CountryCode> markets = new HashSet<>(Arrays.asList(CountryCode.BR, CountryCode.US));
		final TrackDto track1Dto = TrackDto.builder()
			.artists(new HashSet<>(Arrays.asList(artist1Dto)))
			.album(getAlbum1Dto())
			.availableMarkets(markets)
			.discNumber(1)
			.duration(1000)
			.explicit(false)
			.id("trackId1")
			.name("track1")
			.restriction("restriction")
			.trackNumber(2)
			.build();
		final TrackDto track2Dto = TrackDto.builder()
			.artists(new HashSet<>(Arrays.asList(artist1Dto, getArtist2Dto())))
			.album(getAlbum2Dto())
			.availableMarkets(markets)
			.discNumber(1)
			.duration(2000)
			.explicit(true)
			.id("trackId2")
			.name("track2")
			.restriction("restriction")
			.trackNumber(12)
			.build();
		final List<TrackDto> expected = Arrays.asList(track1Dto, track2Dto);

		assertEquals(expected, tracks);
	}

	private void setupTestFindTracks (final List<String> trackIds) {

		final Set<MarketEntity> marketEntities =
			new HashSet<>(Arrays.asList(MarketEntity.builder().code(CountryCode.BR).build(),
				MarketEntity.builder().code(CountryCode.US).build()));
		final ArtistEntity artist1 = getArtist1Entity();
		final TrackEntity track1 = TrackEntity.builder()
			.artists(new HashSet<>(Arrays.asList(artist1)))
			.album(getAlbum1Entity())
			.availableMarkets(marketEntities)
			.discNumber(1)
			.duration(1000)
			.explicit(false)
			.id("trackId1")
			.name("track1")
			.restriction("restriction")
			.trackNumber(2)
			.build();

		final ArtistEntity artist2 = getArtist2Entity();
		final TrackEntity track2 = TrackEntity.builder()
			.artists(new HashSet<>(Arrays.asList(artist1, artist2)))
			.album(getAlbum2Entity())
			.availableMarkets(marketEntities)
			.discNumber(1)
			.duration(2000)
			.explicit(true)
			.id("trackId2")
			.name("track2")
			.restriction("restriction")
			.trackNumber(12)
			.build();

		when(trackRepository.findAllById(trackIds)).thenReturn(Arrays.asList(track1, track2));
	}

	private AlbumEntity getAlbum2Entity () {
		return AlbumEntity.builder()
			.name("album2")
			.id("albumId2")
			.artists(new HashSet<>(Arrays.asList(getArtist2Entity())))
			.build();
	}

	private ArtistEntity getArtist2Entity () {
		return ArtistEntity.builder().name("artist2").id("artistId2").build();
	}

	private AlbumEntity getAlbum1Entity () {
		return AlbumEntity.builder()
			.name("album1")
			.id("albumId1")
			.artists(new HashSet<>(Arrays.asList(getArtist1Entity())))
			.build();
	}

	private ArtistEntity getArtist1Entity () {
		return ArtistEntity.builder().name("artist1").id("artistId1").build();
	}

	private AlbumDto getAlbum2Dto () {
		return AlbumDto.builder()
			.name("album2")
			.id("albumId2")
			.artists(new HashSet<>(Arrays.asList(getArtist2Dto())))
			.build();
	}

	private ArtistDto getArtist2Dto () {
		return ArtistDto.builder().name("artist2").id("artistId2").build();
	}

	private AlbumDto getAlbum1Dto () {
		return AlbumDto.builder()
			.name("album1")
			.id("albumId1")
			.artists(new HashSet<>(Arrays.asList(getArtist1Dto())))
			.build();
	}

	private ArtistDto getArtist1Dto () {
		return ArtistDto.builder().name("artist1").id("artistId1").build();
	}
}
