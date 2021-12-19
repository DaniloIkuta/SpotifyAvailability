package br.daniloikuta.spotifyavailability.business;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import java.time.Clock;
import java.time.LocalDate;
import java.time.ZoneId;
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
import br.daniloikuta.spotifyavailability.dto.TrackAvailabilityResponseDto;
import br.daniloikuta.spotifyavailability.dto.TrackDto;
import br.daniloikuta.spotifyavailability.entity.AlbumEntity;
import br.daniloikuta.spotifyavailability.entity.ArtistEntity;
import br.daniloikuta.spotifyavailability.entity.MarketEntity;
import br.daniloikuta.spotifyavailability.entity.TrackEntity;
import br.daniloikuta.spotifyavailability.repository.AlbumRepository;
import br.daniloikuta.spotifyavailability.repository.TrackRepository;
import br.daniloikuta.spotifyavailability.service.SpotifyService;

@ExtendWith(MockitoExtension.class)
class TrackAvailabilityBusinessTest {
	@InjectMocks
	private TrackAvailabilityBusiness trackAvailabilityBusiness;

	@Mock
	private SpotifyService spotifyService;

	@Mock
	private AlbumRepository albumRepository;

	@Mock
	private TrackRepository trackRepository;

	@Mock
	private Clock clock;
	private final static LocalDate FIXED_DATE = LocalDate.of(2021, 12, 6);

	@Test
	void testGetTrackAvailabilitiesNoneFound () {
		final List<String> trackIds = Arrays.asList("trackId1", "trackId2");
		when(spotifyService.getTracks(trackIds)).thenReturn(new ArrayList<>());

		final TrackAvailabilityResponseDto trackAvailabilities =
			trackAvailabilityBusiness.getTrackAvailabilities(trackIds);

		assertEquals(TrackAvailabilityResponseDto.builder().build(), trackAvailabilities);
		verifyNoInteractions(albumRepository, trackRepository, clock);
	}

	@Test
	void testGetTrackAvailabilities () {
		final List<String> trackIds = Arrays.asList("trackId1", "trackId2");
		setupTestGetTrackAvailabilities(trackIds);

		final TrackAvailabilityResponseDto trackAvailabilities =
			trackAvailabilityBusiness.getTrackAvailabilities(trackIds);

		verify(albumRepository).saveAll(Arrays.asList(getAlbum1Entity(), getAlbum2Entity()));

		final ArtistDto artist1Dto = getArtist1Dto();
		final List<CountryCode> markets = Arrays.asList(CountryCode.BR, CountryCode.US);
		final TrackDto track1Dto = TrackDto.builder()
			.artists(Arrays.asList(artist1Dto))
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
			.artists(Arrays.asList(artist1Dto, getArtist2Dto()))
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
		final TrackAvailabilityResponseDto expected = TrackAvailabilityResponseDto.builder()
			.tracks(Arrays.asList(track1Dto, track2Dto))
			.build();

		assertEquals(expected, trackAvailabilities);
	}

	private void setupTestGetTrackAvailabilities (final List<String> trackIds) {

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
		final List<TrackEntity> allTracks = Arrays.asList(track1, track2);

		when(spotifyService.getTracks(trackIds)).thenReturn(allTracks);
		when(trackRepository.saveAll(allTracks)).thenReturn(allTracks);

		final Clock fixedClock = Clock.fixed(FIXED_DATE.atStartOfDay(ZoneId.systemDefault()).toInstant(),
			ZoneId.systemDefault());
		when(clock.getZone()).thenReturn(fixedClock.getZone());
		when(clock.instant()).thenReturn(fixedClock.instant());
	}

	private AlbumEntity getAlbum2Entity () {
		return AlbumEntity.builder()
			.name("album2")
			.id("albumId2")
			.artists(new HashSet<>(Arrays.asList(getArtist2Entity())))
			.lastUpdated(FIXED_DATE)
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
			.lastUpdated(FIXED_DATE)
			.build();
	}

	private ArtistEntity getArtist1Entity () {
		return ArtistEntity.builder().name("artist1").id("artistId1").build();
	}

	private AlbumDto getAlbum2Dto () {
		return AlbumDto.builder()
			.name("album2")
			.id("albumId2")
			.artists(Arrays.asList(getArtist2Dto()))
			.lastUpdated(FIXED_DATE)
			.build();
	}

	private ArtistDto getArtist2Dto () {
		return ArtistDto.builder().name("artist2").id("artistId2").build();
	}

	private AlbumDto getAlbum1Dto () {
		return AlbumDto.builder()
			.name("album1")
			.id("albumId1")
			.artists(Arrays.asList(getArtist1Dto()))
			.lastUpdated(FIXED_DATE)
			.build();
	}

	private ArtistDto getArtist1Dto () {
		return ArtistDto.builder().name("artist1").id("artistId1").build();
	}
}
