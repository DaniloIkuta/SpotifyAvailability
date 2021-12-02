package br.daniloikuta.spotifyavailability.converter.todto;

import br.daniloikuta.spotifyavailability.dto.ArtistDto;
import br.daniloikuta.spotifyavailability.entity.ArtistEntity;

public class ArtistEntityToDtoConverter {
	public static ArtistDto convert (final ArtistEntity artistEntit) {
		if (artistEntit == null) {
			return null;
		}

		return ArtistDto.builder().id(artistEntit.getId()).name(artistEntit.getName()).build();
	}
}
