package br.daniloikuta.spotifyavailability.converter.toentity;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

import br.daniloikuta.spotifyavailability.entity.AlbumEntity;
import br.daniloikuta.spotifyavailability.entity.ArtistEntity;
import br.daniloikuta.spotifyavailability.entity.MarketEntity;
import br.daniloikuta.spotifyavailability.entity.TrackEntity;
import se.michaelthelin.spotify.model_objects.specification.Track;

public class TrackToTrackEntityConverter {
	private TrackToTrackEntityConverter () {
		throw new IllegalStateException();
	}

	public static TrackEntity convert (final Track track) {
		if (track == null) {
			return null;
		}

		final Set<ArtistEntity> artistEntities = track.getArtists() == null ? null
			: Arrays.asList(track.getArtists())
				.stream()
				.map(ArtistSimplifiedToArtistEntityConverter::convert)
				.collect(Collectors.toSet());

		final Set<MarketEntity> markets = track.getAvailableMarkets() == null ? null
			: Arrays.asList(track.getAvailableMarkets())
				.stream()
				.map(CountryCodeToMarketEntityConverter::convert)
				.collect(Collectors.toSet());

		final AlbumEntity album =
			track.getAlbum() == null ? null : AlbumSimplifiedToAlbumEntityConverter.convert(track.getAlbum());

		final String restriction = track.getRestrictions() == null ? null : track.getRestrictions().getReason();

		return TrackEntity.builder()
			.artists(artistEntities)
			.availableMarkets(markets)
			.discNumber(track.getDiscNumber())
			.trackNumber(track.getTrackNumber())
			.duration(track.getDurationMs())
			.explicit(track.getIsExplicit())
			.name(track.getName())
			.id(track.getId())
			.album(album)
			.restriction(restriction)
			.build();
	}
}
