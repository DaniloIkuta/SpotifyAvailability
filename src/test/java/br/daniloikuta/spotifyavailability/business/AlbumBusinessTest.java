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
import br.daniloikuta.spotifyavailability.entity.CopyrightEntity;
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
		final Set<String> copyrights = new HashSet<>(Arrays.asList("copyrightText"));
		final Set<String> genres = new HashSet<>(Arrays.asList("genre1", "genre2"));
		final AlbumDto album1 = AlbumDto.builder()
			.artists(artists)
			.availableMarkets(markets)
			.copyrights(copyrights)
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
			.copyrights(copyrights)
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
		final Set<CopyrightEntity> copyrights =
			new HashSet<>(Arrays.asList(CopyrightEntity.builder().text("copyrightText").build()));
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
			.copyrights(copyrights)
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
			.copyrights(copyrights)
			.trackCount(2)
			.releaseDate("2021-01-02")
			.releaseDatePrecision(ReleaseDatePrecision.DAY)
			.genres(genres)
			.artists(artists)
			.tracks(new HashSet<>(Arrays.asList(album2Track1, album2Track2)))
			.build();

		when(albumRepository.findAllById(albumIds)).thenReturn(Arrays.asList(album1, album2));
	}
}
