package br.daniloikuta.spotifyavailability.converter.toentity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.Test;

import com.neovisionaries.i18n.CountryCode;

import br.daniloikuta.spotifyavailability.entity.MarketEntity;

class CountryCodeToMarketEntityConverterTest {

	@Test
	void testConvertNull () {
		final MarketEntity marketEntity = CountryCodeToMarketEntityConverter.convert(null);

		assertNull(marketEntity);
	}

	@Test
	void testConvert () {
		final MarketEntity marketEntity = CountryCodeToMarketEntityConverter.convert(CountryCode.BR);

		assertEquals(MarketEntity.builder().code(CountryCode.BR).build(), marketEntity);
	}
}
