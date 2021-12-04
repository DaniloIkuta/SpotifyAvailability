package br.daniloikuta.spotifyavailability.converter.todto;

import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.util.CollectionUtils;

import com.neovisionaries.i18n.CountryCode;

import br.daniloikuta.spotifyavailability.dto.ArtistDto;
import br.daniloikuta.spotifyavailability.dto.TrackDto;
import br.daniloikuta.spotifyavailability.entity.MarketEntity;
import br.daniloikuta.spotifyavailability.entity.TrackEntity;

public class TrackEntityToDtoConverter {
	public static TrackDto convert (final TrackEntity trackEntity) {
		final TrackDto trackDto = convertWithoutAlbum(trackEntity);

		if (trackDto != null && trackEntity.getAlbum() != null) {
			trackDto.setAlbum(AlbumEntityToDtoConverter.convertWithoutTracks(trackEntity.getAlbum()));
		}

		return trackDto;
	}

	public static TrackDto convertWithoutAlbum (final TrackEntity trackEntity) {
		if (trackEntity == null) {
			return null;
		}

		final Set<ArtistDto> artists = CollectionUtils.isEmpty(trackEntity.getArtists()) ? null
			: trackEntity.getArtists().stream().map(ArtistEntityToDtoConverter::convert).collect(Collectors.toSet());
		final Set<CountryCode> markets = CollectionUtils.isEmpty(trackEntity.getAvailableMarkets()) ? null
			: trackEntity.getAvailableMarkets().stream().map(MarketEntity::getCode).collect(Collectors.toSet());

		return TrackDto.builder()
			.artists(artists)
			.availableMarkets(markets)
			.discNumber(trackEntity.getDiscNumber())
			.duration(trackEntity.getDuration())
			.explicit(trackEntity.getExplicit())
			.id(trackEntity.getId())
			.name(trackEntity.getName())
			.restriction(trackEntity.getRestriction())
			.trackNumber(trackEntity.getTrackNumber())
			.build();
	}
}
