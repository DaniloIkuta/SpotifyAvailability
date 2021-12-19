package br.daniloikuta.spotifyavailability.converter.toentity;

import br.daniloikuta.spotifyavailability.entity.ArtistEntity;
import se.michaelthelin.spotify.model_objects.specification.ArtistSimplified;

public class ArtistSimplifiedToArtistEntityConverter {
	private ArtistSimplifiedToArtistEntityConverter () {
		throw new IllegalStateException();
	}

	public static ArtistEntity convert (final ArtistSimplified artistSimplified) {
		return artistSimplified == null ? null
			: ArtistEntity.builder().name(artistSimplified.getName()).id(artistSimplified.getId()).build();
	}
}
