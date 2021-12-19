package br.daniloikuta.spotifyavailability.converter.toentity;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

import br.daniloikuta.spotifyavailability.entity.ArtistEntity;
import br.daniloikuta.spotifyavailability.entity.MarketEntity;
import br.daniloikuta.spotifyavailability.entity.TrackEntity;
import se.michaelthelin.spotify.model_objects.specification.TrackSimplified;

public class TrackSimplifiedToTrackEntityConverter {
	private TrackSimplifiedToTrackEntityConverter () {
		throw new IllegalStateException();
	}

	public static TrackEntity convert (final TrackSimplified trackSimplified) {
		if (trackSimplified == null) {
			return null;
		}

		final Set<ArtistEntity> artistEntities = trackSimplified.getArtists() == null ? null
			: Arrays.asList(trackSimplified.getArtists())
				.stream()
				.map(ArtistSimplifiedToArtistEntityConverter::convert)
				.collect(Collectors.toSet());

		final Set<MarketEntity> markets = trackSimplified.getAvailableMarkets() == null ? null
			: Arrays.asList(trackSimplified.getAvailableMarkets())
				.stream()
				.map(CountryCodeToMarketEntityConverter::convert)
				.collect(Collectors.toSet());

		return TrackEntity.builder()
			.artists(artistEntities)
			.availableMarkets(markets)
			.discNumber(trackSimplified.getDiscNumber())
			.trackNumber(trackSimplified.getTrackNumber())
			.duration(trackSimplified.getDurationMs())
			.explicit(trackSimplified.getIsExplicit())
			.name(trackSimplified.getName())
			.id(trackSimplified.getId())
			.build();
	}
}
