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
import br.daniloikuta.spotifyavailability.entity.MarketEntity;
import se.michaelthelin.spotify.enums.AlbumType;
import se.michaelthelin.spotify.model_objects.miscellaneous.Restrictions;
import se.michaelthelin.spotify.model_objects.specification.AlbumSimplified;
import se.michaelthelin.spotify.model_objects.specification.ArtistSimplified;

class AlbumSimplifiedToAlbumEntityConverterTest {

	@Test
	void testConvertNull () {
		final AlbumEntity albumEntity = AlbumSimplifiedToAlbumEntityConverter.convert(null);

		assertNull(albumEntity);
	}

	@Test
	void testConvertNullProperties () {
		final AlbumEntity albumEntity =
			AlbumSimplifiedToAlbumEntityConverter.convert(new AlbumSimplified.Builder().build());

		assertEquals(AlbumEntity.builder().build(), albumEntity);
	}

	@Test
	void testConvert () {
		final AlbumSimplified albumSimplified = new AlbumSimplified.Builder().setAlbumType(AlbumType.ALBUM)
			.setArtists(new ArtistSimplified.Builder().setId("artistSpotifyId").setName("artistName").build())
			.setAvailableMarkets(CountryCode.BR, CountryCode.US)
			.setRestrictions(new Restrictions.Builder().setReason("reason").build())
			.setId("albumSpotifyId")
			.setName("albumName")
			.build();

		final AlbumEntity albumEntity = AlbumSimplifiedToAlbumEntityConverter.convert(albumSimplified);

		final Set<MarketEntity> availableMarkets = new HashSet<>(Arrays.asList(
			MarketEntity.builder().code(CountryCode.BR).build(),
			MarketEntity.builder().code(CountryCode.US).build()));
		final Set<ArtistEntity> artists =
			new HashSet<>(Arrays.asList(ArtistEntity.builder().id("artistSpotifyId").name("artistName").build()));
		final AlbumEntity expected = AlbumEntity.builder()
			.id("albumSpotifyId")
			.name("albumName")
			.type(br.daniloikuta.spotifyavailability.enums.AlbumType.ALBUM)
			.availableMarkets(availableMarkets)
			.artists(artists)
			.restriction("reason")
			.build();

		assertEquals(expected, albumEntity);
	}
}
