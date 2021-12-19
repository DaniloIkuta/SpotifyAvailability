package br.daniloikuta.spotifyavailability.converter.toentity;

import java.util.Arrays;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import br.daniloikuta.spotifyavailability.entity.AlbumEntity;
import br.daniloikuta.spotifyavailability.entity.ArtistEntity;
import br.daniloikuta.spotifyavailability.entity.GenreEntity;
import br.daniloikuta.spotifyavailability.entity.MarketEntity;
import br.daniloikuta.spotifyavailability.entity.TrackEntity;
import br.daniloikuta.spotifyavailability.enums.AlbumType;
import br.daniloikuta.spotifyavailability.enums.ReleaseDatePrecision;
import se.michaelthelin.spotify.model_objects.specification.Album;
import se.michaelthelin.spotify.model_objects.specification.Paging;
import se.michaelthelin.spotify.model_objects.specification.TrackSimplified;

public class AlbumToAlbumEntityConverter {
	private AlbumToAlbumEntityConverter () {
		throw new IllegalStateException();
	}

	public static AlbumEntity convert (final Album album) {
		if (album == null) {
			return null;
		}

		final String type = album.getAlbumType() == null ? null : album.getAlbumType().type;
		final AlbumType albumType = type == null ? null : AlbumType.keyOf(type);

		final Set<MarketEntity> marketEntities = album.getAvailableMarkets() == null ? null
			: Arrays.asList(album.getAvailableMarkets())
				.stream()
				.map(CountryCodeToMarketEntityConverter::convert)
				.collect(Collectors.toSet());

		final String copyrights = album.getCopyrights() == null ? null
			: Arrays.asList(album.getCopyrights())
				.stream()
				.map(copyright -> copyright == null ? null : copyright.getText())
				.filter(Objects::nonNull)
				.collect(Collectors.joining(", "));

		final Paging<TrackSimplified> tracksPaging = album.getTracks();
		final Set<TrackEntity> trackEntities = tracksPaging == null || tracksPaging.getItems() == null ? null
			: Arrays.asList(tracksPaging.getItems())
				.stream()
				.map(TrackSimplifiedToTrackEntityConverter::convert)
				.collect(Collectors.toSet());
		final Integer total = tracksPaging == null ? null : tracksPaging.getTotal();

		final String releaseDate = album.getReleaseDate() == null ? null : album.getReleaseDate();
		final ReleaseDatePrecision releaseDatePrecision = album.getReleaseDatePrecision() == null ? null
			: ReleaseDatePrecision.keyOf(album.getReleaseDatePrecision().getPrecision());

		final Set<GenreEntity> genreEntities = album.getGenres() == null ? null
			: Arrays.asList(album.getGenres())
				.stream()
				.map(genre -> GenreEntity.builder().genre(genre).build())
				.collect(Collectors.toSet());

		final Set<ArtistEntity> artistEntities = album.getArtists() == null ? null
			: Arrays.asList(album.getArtists())
				.stream()
				.map(ArtistSimplifiedToArtistEntityConverter::convert)
				.collect(Collectors.toSet());

		return AlbumEntity.builder()
			.id(album.getId())
			.name(album.getName())
			.type(albumType)
			.availableMarkets(marketEntities)
			.copyrights(copyrights)
			.trackCount(total)
			.releaseDate(releaseDate)
			.releaseDatePrecision(releaseDatePrecision)
			.genres(genreEntities)
			.artists(artistEntities)
			.tracks(trackEntities)
			.build();
	}
}
