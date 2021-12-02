package br.daniloikuta.spotifyavailability.converter.toentity;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

import br.daniloikuta.spotifyavailability.entity.AlbumEntity;
import br.daniloikuta.spotifyavailability.entity.ArtistEntity;
import br.daniloikuta.spotifyavailability.entity.MarketEntity;
import br.daniloikuta.spotifyavailability.enums.AlbumType;
import se.michaelthelin.spotify.model_objects.specification.AlbumSimplified;

public class AlbumSimplifiedToAlbumEntityConverter {
	public static AlbumEntity convert (final AlbumSimplified albumSimplified) {
		if (albumSimplified == null) {
			return null;
		}

		final String type = albumSimplified.getAlbumType() == null ? null : albumSimplified.getAlbumType().type;
		final AlbumType albumType = type == null ? null : AlbumType.keyOf(type);

		final Set<MarketEntity> marketEntities = albumSimplified.getAvailableMarkets() == null ? null
			: Arrays.asList(albumSimplified.getAvailableMarkets())
				.stream()
				.map(CountryCodeToMarketEntityConverter::convert)
				.collect(Collectors.toSet());

		final Set<ArtistEntity> artistEntities = albumSimplified.getArtists() == null ? null
			: Arrays.asList(albumSimplified.getArtists())
				.stream()
				.map(ArtistSimplifiedToArtistEntityConverter::convert)
				.collect(Collectors.toSet());

		final String restriction =
			albumSimplified.getRestrictions() == null ? null : albumSimplified.getRestrictions().getReason();

		return AlbumEntity.builder()
			.id(albumSimplified.getId())
			.name(albumSimplified.getName())
			.type(albumType)
			.availableMarkets(marketEntities)
			.artists(artistEntities)
			.restriction(restriction)
			.build();
	}
}
