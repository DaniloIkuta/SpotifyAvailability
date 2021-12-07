package br.daniloikuta.spotifyavailability.converter.toentity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.Test;

import com.neovisionaries.i18n.CountryCode;

import br.daniloikuta.spotifyavailability.entity.AlbumEntity;
import br.daniloikuta.spotifyavailability.entity.ArtistEntity;
import br.daniloikuta.spotifyavailability.entity.GenreEntity;
import br.daniloikuta.spotifyavailability.entity.MarketEntity;
import br.daniloikuta.spotifyavailability.entity.TrackEntity;
import se.michaelthelin.spotify.enums.AlbumType;
import se.michaelthelin.spotify.enums.ReleaseDatePrecision;
import se.michaelthelin.spotify.model_objects.specification.Album;
import se.michaelthelin.spotify.model_objects.specification.ArtistSimplified;
import se.michaelthelin.spotify.model_objects.specification.Copyright;
import se.michaelthelin.spotify.model_objects.specification.Paging;
import se.michaelthelin.spotify.model_objects.specification.Paging.Builder;
import se.michaelthelin.spotify.model_objects.specification.TrackSimplified;

public class AlbumToAlbumEntityConverterTest {
	@Test
	void testConvertNull () {
		final AlbumEntity albumEntity = AlbumToAlbumEntityConverter.convert(null);

		assertNull(albumEntity);
	}

	@Test
	void testConvertNullProperties () {
		final AlbumEntity albumEntity = AlbumToAlbumEntityConverter.convert(new Album.Builder().build());

		assertEquals(AlbumEntity.builder().build(), albumEntity);
	}

	@Test
	void testConvert () {
		final Paging<TrackSimplified> trackPaging = setupTracks();

		final Album album = new Album.Builder().setAlbumType(AlbumType.ALBUM)
			.setArtists(new ArtistSimplified.Builder().setId("artistSpotifyId").setName("artistName").build())
			.setAvailableMarkets(CountryCode.BR, CountryCode.US)
			.setId("albumSpotifyId")
			.setName("albumName")
			.setCopyrights(new Copyright.Builder().setText("copyrightText1").build(),
				null,
				new Copyright.Builder().setText("copyrightText2").build())
			.setReleaseDate("2021-01-01")
			.setReleaseDatePrecision(ReleaseDatePrecision.YEAR)
			.setGenres("genre1", "genre2")
			.setTracks(trackPaging)
			.build();

		final AlbumEntity albumEntity = AlbumToAlbumEntityConverter.convert(album);

		final Set<MarketEntity> availableMarkets = new HashSet<>(Arrays.asList(
			MarketEntity.builder().code(CountryCode.BR).build(),
			MarketEntity.builder().code(CountryCode.US).build()));
		final Set<ArtistEntity> artists =
			new HashSet<>(Arrays.asList(ArtistEntity.builder().id("artistSpotifyId").name("artistName").build()));
		final Set<GenreEntity> genres = new HashSet<>(Arrays.asList(GenreEntity.builder().genre("genre1").build(),
			GenreEntity.builder().genre("genre2").build()));
		final Set<TrackEntity> tracks =
			new HashSet<>(Arrays.asList(
				TrackEntity.builder()
					.name("track1")
					.build(),
				TrackEntity.builder()
					.name("track2")
					.build()));
		final AlbumEntity expected = AlbumEntity.builder()
			.id("albumSpotifyId")
			.name("albumName")
			.type(br.daniloikuta.spotifyavailability.enums.AlbumType.ALBUM)
			.availableMarkets(availableMarkets)
			.artists(artists)
			.copyrights("copyrightText1, copyrightText2")
			.releaseDate("2021-01-01")
			.releaseDatePrecision(br.daniloikuta.spotifyavailability.enums.ReleaseDatePrecision.YEAR)
			.genres(genres)
			.trackCount(20)
			.tracks(tracks)
			.build();

		assertEquals(expected, albumEntity);
	}

	private Paging<TrackSimplified> setupTracks () {
		final TrackSimplified[] tracks =
			{ new TrackSimplified.Builder().setName("track1").build(),
				new TrackSimplified.Builder().setName("track2").build() };
		final Paging<TrackSimplified> trackPaging = new Builder<TrackSimplified>().setTotal(20)
			.setItems(tracks)
			.build();
		return trackPaging;
	}
}
