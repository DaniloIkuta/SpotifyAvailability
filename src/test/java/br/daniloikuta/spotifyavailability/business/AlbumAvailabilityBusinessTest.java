package br.daniloikuta.spotifyavailability.business;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.never;
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
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import com.neovisionaries.i18n.CountryCode;

import br.daniloikuta.spotifyavailability.dto.AlbumAvailabilityResponseDto;
import br.daniloikuta.spotifyavailability.dto.AlbumDto;
import br.daniloikuta.spotifyavailability.dto.ArtistDto;
import br.daniloikuta.spotifyavailability.entity.AlbumEntity;
import br.daniloikuta.spotifyavailability.entity.ArtistEntity;
import br.daniloikuta.spotifyavailability.entity.GenreEntity;
import br.daniloikuta.spotifyavailability.entity.MarketEntity;
import br.daniloikuta.spotifyavailability.entity.TrackEntity;
import br.daniloikuta.spotifyavailability.enums.AlbumType;
import br.daniloikuta.spotifyavailability.enums.ReleaseDatePrecision;
import br.daniloikuta.spotifyavailability.repository.AlbumRepository;
import br.daniloikuta.spotifyavailability.repository.TrackRepository;
import br.daniloikuta.spotifyavailability.service.SpotifyService;

@ExtendWith(MockitoExtension.class)
public class AlbumAvailabilityBusinessTest {
	@InjectMocks
	@Spy
	private AlbumAvailabilityBusiness albumAvailabilityBusiness;

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
	void testGetAlbumAvailabilitiesNoneFound () {
		final List<String> albumIds = Arrays.asList("albumId1", "albumId2");
		when(spotifyService.getAlbums(albumIds)).thenReturn(new ArrayList<>());

		final AlbumAvailabilityResponseDto albumAvailabilities =
			albumAvailabilityBusiness.getAlbumAvailabilities(albumIds);

		assertEquals(AlbumAvailabilityResponseDto.builder().build(), albumAvailabilities);
		verifyNoInteractions(albumRepository, trackRepository, clock);
	}

	@Test
	void testGetAlbumAvailabilities () {
		final List<String> albumIds = Arrays.asList("albumId1", "albumId2");
		final Set<TrackEntity> allTracksWithAlbum = setupTestGetAlbumAvailabilities(albumIds);

		final AlbumAvailabilityResponseDto albumAvailabilities =
			albumAvailabilityBusiness.getAlbumAvailabilities(albumIds);

		verify(trackRepository).saveAll(allTracksWithAlbum);

		final ArtistDto artist = ArtistDto.builder().id("artistId").name("artistName").build();
		final List<CountryCode> markets = Arrays.asList(CountryCode.BR, CountryCode.US);
		final List<String> genres = Arrays.asList("genre1", "genre2");
		final AlbumDto album1 = AlbumDto.builder()
			.artists(Arrays.asList(artist))
			.availableMarkets(markets)
			.copyrights("copyrights")
			.genres(genres)
			.id("albumId1")
			.name("album1")
			.releaseDate("2021-01-01")
			.releaseDatePrecision(ReleaseDatePrecision.DAY)
			.trackCount(1)
			.type(AlbumType.ALBUM)
			.lastUpdated(FIXED_DATE)
			.build();
		final AlbumDto album2 = AlbumDto.builder()
			.artists(Arrays.asList(artist))
			.availableMarkets(markets)
			.copyrights("copyrights")
			.genres(genres)
			.id("albumId2")
			.name("album2")
			.releaseDate("2021-01-02")
			.releaseDatePrecision(ReleaseDatePrecision.DAY)
			.trackCount(2)
			.type(AlbumType.ALBUM)
			.lastUpdated(FIXED_DATE)
			.build();
		final AlbumAvailabilityResponseDto expected =
			AlbumAvailabilityResponseDto.builder().albums(Arrays.asList(album1, album2)).build();

		assertEquals(expected, albumAvailabilities);
	}

	private Set<TrackEntity> setupTestGetAlbumAvailabilities (final List<String> albumIds) {
		final Set<MarketEntity> marketEntities =
			new HashSet<>(Arrays.asList(MarketEntity.builder().code(CountryCode.BR).build(),
				MarketEntity.builder().code(CountryCode.US).build()));
		final Set<ArtistEntity> artists =
			new HashSet<>(Arrays.asList(ArtistEntity.builder().id("artistId").name("artistName").build()));
		final Set<GenreEntity> genres = new HashSet<>(Arrays.asList(GenreEntity.builder().genre("genre1").build(),
			GenreEntity.builder().genre("genre2").build()));

		final AlbumEntity.AlbumEntityBuilder albumBuilder1 = AlbumEntity.builder()
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
			.lastUpdated(FIXED_DATE);

		final AlbumEntity.AlbumEntityBuilder albumBuilder2 = AlbumEntity.builder()
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
			.lastUpdated(FIXED_DATE);

		final TrackEntity.TrackEntityBuilder album1Track1Builder =
			TrackEntity.builder().id("trackId1").name("track1Album1").artists(artists);
		final TrackEntity.TrackEntityBuilder album2Track1Builder =
			TrackEntity.builder().id("trackId2").name("track1Album2").artists(artists);
		final TrackEntity.TrackEntityBuilder album2Track2Builder =
			TrackEntity.builder().id("trackId3").name("track2Album2").artists(artists);

		final List<AlbumEntity> albumWithoutTracks = Arrays.asList(albumBuilder1.build(), albumBuilder2.build());
		final List<AlbumEntity> albumEntities =
			Arrays.asList(albumBuilder1.tracks(new HashSet<>(Arrays.asList(album1Track1Builder.build()))).build(),
				albumBuilder2
					.tracks(new HashSet<>(Arrays.asList(album2Track1Builder.build(), album2Track2Builder.build())))
					.build());

		when(spotifyService.getAlbums(albumIds)).thenReturn(albumEntities);
		when(albumRepository.saveAll(albumWithoutTracks)).thenReturn(albumWithoutTracks);

		final Clock fixedClock = Clock.fixed(FIXED_DATE.atStartOfDay(ZoneId.systemDefault()).toInstant(),
			ZoneId.systemDefault());
		when(clock.getZone()).thenReturn(fixedClock.getZone());
		when(clock.instant()).thenReturn(fixedClock.instant());

		return new HashSet<>(Arrays.asList(album1Track1Builder.album(albumWithoutTracks.get(0)).build(),
			album2Track1Builder.album(albumWithoutTracks.get(1)).build(),
			album2Track2Builder.album(albumWithoutTracks.get(1)).build()));
	}

	@Test
	void testFetchMissingTracksNoneFound () {
		when(albumRepository.findAlbumIdsWithoutTracks()).thenReturn(new ArrayList<>());

		albumAvailabilityBusiness.fetchMissingTracks();

		verify(albumAvailabilityBusiness, never()).getAlbumAvailabilities(anyList());
		verifyNoInteractions(trackRepository, clock);
	}

	@Test
	void testFetchMissingTracks () {
		final List<String> ids = Arrays.asList("albumId1", "albumId2");
		when(albumRepository.findAlbumIdsWithoutTracks()).thenReturn(ids);

		albumAvailabilityBusiness.fetchMissingTracks();

		verify(albumAvailabilityBusiness).getAlbumAvailabilities(ids);
		verifyNoInteractions(trackRepository, clock);
	}

	@Test
	void testRefreshAvailabilitiesNoneFound () {
		ReflectionTestUtils.setField(albumAvailabilityBusiness, "refreshIntervalDays", 7);
		when(albumRepository.findAlbumIdsToRefresh(7)).thenReturn(new ArrayList<>());

		albumAvailabilityBusiness.refreshAvailabilities();

		verify(albumAvailabilityBusiness, never()).getAlbumAvailabilities(anyList());
		verifyNoInteractions(trackRepository, clock);
	}

	@Test
	void testRefreshAvailabilities () {
		ReflectionTestUtils.setField(albumAvailabilityBusiness, "refreshIntervalDays", 7);
		final List<String> ids = Arrays.asList("albumId1", "albumId2");
		when(albumRepository.findAlbumIdsToRefresh(7)).thenReturn(ids);

		albumAvailabilityBusiness.refreshAvailabilities();

		verify(albumAvailabilityBusiness).getAlbumAvailabilities(ids);
		verifyNoInteractions(trackRepository, clock);
	}
}
