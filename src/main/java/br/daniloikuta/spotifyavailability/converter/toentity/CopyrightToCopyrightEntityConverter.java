package br.daniloikuta.spotifyavailability.converter.toentity;

import br.daniloikuta.spotifyavailability.entity.CopyrightEntity;
import se.michaelthelin.spotify.model_objects.specification.Copyright;

public class CopyrightToCopyrightEntityConverter {
	public static CopyrightEntity convert (final Copyright copyright) {
		return copyright == null ? null : CopyrightEntity.builder().text(copyright.getText()).build();
	}
}
