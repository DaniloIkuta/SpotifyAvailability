package br.daniloikuta.spotifyavailability.converter.toentity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.Test;

import br.daniloikuta.spotifyavailability.entity.ArtistEntity;
import se.michaelthelin.spotify.model_objects.specification.ArtistSimplified;

public class ArtistSimplifiedToArtistEntityConverterTest {
	@Test
	void testConvertNull () {
		final ArtistEntity artistEntity = ArtistSimplifiedToArtistEntityConverter.convert(null);

		assertNull(artistEntity);
	}

	@Test
	void testConvertNullProperties () {
		final ArtistSimplified artistSimplified =
			new ArtistSimplified.Builder().setName(null).setId(null).build();
		final ArtistEntity artistEntity = ArtistSimplifiedToArtistEntityConverter.convert(artistSimplified);

		assertEquals(ArtistEntity.builder().name(null).id(null).build(), artistEntity);
	}

	@Test
	void testConvert () {
		final ArtistSimplified artistSimplified =
			new ArtistSimplified.Builder().setName("artistName").setId("id").build();
		final ArtistEntity artistEntity = ArtistSimplifiedToArtistEntityConverter.convert(artistSimplified);

		assertEquals(ArtistEntity.builder().name("artistName").id("id").build(), artistEntity);
	}
}
