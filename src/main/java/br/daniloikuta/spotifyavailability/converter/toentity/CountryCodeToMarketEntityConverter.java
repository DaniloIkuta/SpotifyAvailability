package br.daniloikuta.spotifyavailability.converter.toentity;

import com.neovisionaries.i18n.CountryCode;

import br.daniloikuta.spotifyavailability.entity.MarketEntity;

public class CountryCodeToMarketEntityConverter {
	private CountryCodeToMarketEntityConverter () {
		throw new IllegalStateException();
	}

	public static MarketEntity convert (final CountryCode countryCode) {
		return countryCode == null ? null : MarketEntity.builder().code(countryCode).build();
	}
}
