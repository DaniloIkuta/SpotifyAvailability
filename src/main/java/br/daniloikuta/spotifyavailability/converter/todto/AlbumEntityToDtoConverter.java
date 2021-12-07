package br.daniloikuta.spotifyavailability.converter.todto;

import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.util.CollectionUtils;

import com.neovisionaries.i18n.CountryCode;

import br.daniloikuta.spotifyavailability.dto.AlbumDto;
import br.daniloikuta.spotifyavailability.dto.ArtistDto;
import br.daniloikuta.spotifyavailability.entity.AlbumEntity;
import br.daniloikuta.spotifyavailability.entity.GenreEntity;
import br.daniloikuta.spotifyavailability.entity.MarketEntity;

public class AlbumEntityToDtoConverter {
	public static AlbumDto convert (final AlbumEntity albumEntity) {
		final AlbumDto albumDto = convertWithoutTracks(albumEntity);

		if (albumDto != null && !CollectionUtils.isEmpty(albumEntity.getTracks())) {
			albumDto.setTracks(
				albumEntity.getTracks()
					.stream()
					.map(TrackEntityToDtoConverter::convertWithoutAlbum)
					.collect(Collectors.toSet()));
		}

		return albumDto;
	}

	public static AlbumDto convertWithoutTracks (final AlbumEntity albumEntity) {
		if (albumEntity == null) {
			return null;
		}

		final Set<ArtistDto> artists = CollectionUtils.isEmpty(albumEntity.getArtists()) ? null
			: albumEntity.getArtists().stream().map(ArtistEntityToDtoConverter::convert).collect(Collectors.toSet());
		final Set<CountryCode> markets = CollectionUtils.isEmpty(albumEntity.getAvailableMarkets()) ? null
			: albumEntity.getAvailableMarkets()
				.stream()
				.map(MarketEntity::getCode)
				.collect(Collectors.toSet());
		final Set<String> genres = CollectionUtils.isEmpty(albumEntity.getGenres()) ? null
			: albumEntity.getGenres().stream().map(GenreEntity::getGenre).collect(Collectors.toSet());

		return AlbumDto.builder()
			.artists(artists)
			.availableMarkets(markets)
			.copyrights(albumEntity.getCopyrights())
			.genres(genres)
			.id(albumEntity.getId())
			.name(albumEntity.getName())
			.releaseDate(albumEntity.getReleaseDate())
			.releaseDatePrecision(albumEntity.getReleaseDatePrecision())
			.restriction(albumEntity.getRestriction())
			.trackCount(albumEntity.getTrackCount())
			.type(albumEntity.getType())
			.lastUpdated(albumEntity.getLastUpdated())
			.build();
	}
}
