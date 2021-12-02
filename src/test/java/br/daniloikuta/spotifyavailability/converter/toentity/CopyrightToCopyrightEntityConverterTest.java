package br.daniloikuta.spotifyavailability.converter.toentity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.Test;

import br.daniloikuta.spotifyavailability.entity.CopyrightEntity;
import se.michaelthelin.spotify.model_objects.specification.Copyright;

public class CopyrightToCopyrightEntityConverterTest {

	@Test
	void testConvertNull () {
		final CopyrightEntity copyrightEntity = CopyrightToCopyrightEntityConverter.convert(null);

		assertNull(copyrightEntity);
	}

	@Test
	void testConvertNullProperties () {
		final Copyright copyright = new Copyright.Builder().setText(null).build();
		final CopyrightEntity copyrightEntity = CopyrightToCopyrightEntityConverter.convert(copyright);

		assertEquals(CopyrightEntity.builder().text(null).build(), copyrightEntity);
	}

	@Test
	void testConvert () {
		final Copyright copyright = new Copyright.Builder().setText("aaa").build();
		final CopyrightEntity copyrightEntity = CopyrightToCopyrightEntityConverter.convert(copyright);

		assertEquals(CopyrightEntity.builder().text("aaa").build(), copyrightEntity);
	}
}
