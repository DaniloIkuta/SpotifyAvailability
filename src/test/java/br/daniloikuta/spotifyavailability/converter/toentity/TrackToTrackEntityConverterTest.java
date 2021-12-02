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
import br.daniloikuta.spotifyavailability.entity.TrackEntity;
import se.michaelthelin.spotify.model_objects.miscellaneous.Restrictions;
import se.michaelthelin.spotify.model_objects.specification.AlbumSimplified;
import se.michaelthelin.spotify.model_objects.specification.ArtistSimplified;
import se.michaelthelin.spotify.model_objects.specification.Track;

public class TrackToTrackEntityConverterTest {
	@Test
	void testConvertNull () {
		final TrackEntity trackEntity = TrackToTrackEntityConverter.convert(null);

		assertNull(trackEntity);
	}

	@Test
	void testConvertNullProperties () {
		final TrackEntity trackEntity =
			TrackToTrackEntityConverter.convert(new Track.Builder().build());

		assertEquals(TrackEntity.builder().build(), trackEntity);
	}

	@Test
	void testConvert () {
		final Track Track = new Track.Builder()
			.setArtists(new ArtistSimplified.Builder().setId("artistSpotifyId").setName("artistName").build())
			.setAvailableMarkets(CountryCode.BR, CountryCode.US)
			.setDiscNumber(1)
			.setTrackNumber(2)
			.setDurationMs(60000)
			.setExplicit(false)
			.setName("trackName")
			.setId("trackSpotifyId")
			.setAlbum(new AlbumSimplified.Builder().setName("albumName").build())
			.setRestrictions(new Restrictions.Builder().setReason("reason").build())
			.build();

		final TrackEntity trackEntity = TrackToTrackEntityConverter.convert(Track);

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
			.album(AlbumEntity.builder().name("albumName").build())
			.restriction("reason")
			.build();

		assertEquals(expected, trackEntity);
	}
}
