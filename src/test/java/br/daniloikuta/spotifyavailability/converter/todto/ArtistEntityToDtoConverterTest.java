package br.daniloikuta.spotifyavailability.converter.todto;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.Test;

import br.daniloikuta.spotifyavailability.dto.ArtistDto;
import br.daniloikuta.spotifyavailability.entity.ArtistEntity;

public class ArtistEntityToDtoConverterTest {

	@Test
	void testConvertNull () {
		final ArtistDto artistDto = ArtistEntityToDtoConverter.convert(null);

		assertNull(artistDto);
	}

	@Test
	void testConvertNullProperties () {
		final ArtistDto artistDto = ArtistEntityToDtoConverter.convert(ArtistEntity.builder().build());

		assertEquals(ArtistDto.builder().build(), artistDto);
	}

	@Test
	void testConvert () {
		final ArtistDto artistDto =
			ArtistEntityToDtoConverter.convert(ArtistEntity.builder().id("artistId").name("artistName").build());

		assertEquals(ArtistDto.builder().id("artistId").name("artistName").build(), artistDto);
	}
}
