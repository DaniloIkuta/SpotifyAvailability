package br.daniloikuta.spotifyavailability.converter.todto;

import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.util.CollectionUtils;

import com.neovisionaries.i18n.CountryCode;

import br.daniloikuta.spotifyavailability.dto.AlbumDto;
import br.daniloikuta.spotifyavailability.dto.ArtistDto;
import br.daniloikuta.spotifyavailability.dto.TrackDto;
import br.daniloikuta.spotifyavailability.entity.AlbumEntity;
import br.daniloikuta.spotifyavailability.entity.CopyrightEntity;
import br.daniloikuta.spotifyavailability.entity.GenreEntity;
import br.daniloikuta.spotifyavailability.entity.MarketEntity;

public class AlbumEntityToDtoConverter {
	public static AlbumDto convert (final AlbumEntity albumEntity) {
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
		final Set<String> copyrights = CollectionUtils.isEmpty(albumEntity.getCopyrights()) ? null
			: albumEntity.getCopyrights().stream().map(CopyrightEntity::getText).collect(Collectors.toSet());
		final Set<String> genres = CollectionUtils.isEmpty(albumEntity.getGenres()) ? null
			: albumEntity.getGenres().stream().map(GenreEntity::getGenre).collect(Collectors.toSet());
		final Set<TrackDto> tracks = CollectionUtils.isEmpty(albumEntity.getTracks()) ? null
			: albumEntity.getTracks().stream().map(TrackEntityToDtoConverter::convert).collect(Collectors.toSet());

		return AlbumDto.builder()
			.artists(artists)
			.availableMarkets(markets)
			.copyrights(copyrights)
			.genres(genres)
			.id(albumEntity.getId())
			.name(albumEntity.getName())
			.releaseDate(albumEntity.getReleaseDate())
			.releaseDatePrecision(albumEntity.getReleaseDatePrecision())
			.restriction(albumEntity.getRestriction())
			.trackCount(albumEntity.getTrackCount())
			.tracks(tracks)
			.type(albumEntity.getType())
			.build();
	}
}
