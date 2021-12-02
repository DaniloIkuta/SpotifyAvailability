package br.daniloikuta.spotifyavailability.converter.toentity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.Test;

import com.neovisionaries.i18n.CountryCode;

import br.daniloikuta.spotifyavailability.entity.ArtistEntity;
import br.daniloikuta.spotifyavailability.entity.MarketEntity;
import br.daniloikuta.spotifyavailability.entity.TrackEntity;
import se.michaelthelin.spotify.model_objects.specification.ArtistSimplified;
import se.michaelthelin.spotify.model_objects.specification.TrackSimplified;

public class TrackSimplifiedToTrackEntityConverterTest {

	@Test
	void testConvertNull () {
		final TrackEntity trackEntity = TrackSimplifiedToTrackEntityConverter.convert(null);

		assertNull(trackEntity);
	}

	@Test
	void testConvertNullProperties () {
		final TrackEntity trackEntity =
			TrackSimplifiedToTrackEntityConverter.convert(new TrackSimplified.Builder().build());

		assertEquals(TrackEntity.builder().build(), trackEntity);
	}

	@Test
	void testConvert () {
		final TrackSimplified trackSimplified = new TrackSimplified.Builder()
			.setArtists(new ArtistSimplified.Builder().setId("artistSpotifyId").setName("artistName").build())
			.setAvailableMarkets(CountryCode.BR, CountryCode.US)
			.setDiscNumber(1)
			.setTrackNumber(2)
			.setDurationMs(60000)
			.setExplicit(false)
			.setName("trackName")
			.setId("trackSpotifyId")
			.build();

		final TrackEntity trackEntity = TrackSimplifiedToTrackEntityConverter.convert(trackSimplified);

		final Set<MarketEntity> availableMarkets = new HashSet<>(Arrays.asList(
			MarketEntity.builder().code(CountryCode.BR).build(),
			MarketEntity.builder().code(CountryCode.US).build()));
		final Set<ArtistEntity> artists =
			new HashSet<>(Arrays.asList(ArtistEntity.builder().id("artistSpotifyId").name("artistName").build()));
		final TrackEntity expected = TrackEntity.builder()
			.artists(artists)
			.availableMarkets(availableMarkets)
			.discNumber(1)
			.trackNumber(2)
			.duration(60000)
			.explicit(false)
			.name("trackName")
			.id("trackSpotifyId")
			.build();

		assertEquals(expected, trackEntity);
	}
}
