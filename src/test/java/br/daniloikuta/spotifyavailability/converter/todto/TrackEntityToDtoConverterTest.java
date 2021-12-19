package br.daniloikuta.spotifyavailability.converter.todto;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.util.Arrays;
import java.util.HashSet;

import org.junit.jupiter.api.Test;

import com.neovisionaries.i18n.CountryCode;

import br.daniloikuta.spotifyavailability.dto.AlbumDto;
import br.daniloikuta.spotifyavailability.dto.ArtistDto;
import br.daniloikuta.spotifyavailability.dto.TrackDto;
import br.daniloikuta.spotifyavailability.entity.AlbumEntity;
import br.daniloikuta.spotifyavailability.entity.ArtistEntity;
import br.daniloikuta.spotifyavailability.entity.MarketEntity;
import br.daniloikuta.spotifyavailability.entity.TrackEntity;

class TrackEntityToDtoConverterTest {
	@Test
	void testConvertNull () {
		final TrackDto trackDto = TrackEntityToDtoConverter.convert(null);

		assertNull(trackDto);
	}

	@Test
	void testConvertNullProperties () {
		final TrackDto trackDto = TrackEntityToDtoConverter.convert(TrackEntity.builder().build());

		assertEquals(TrackDto.builder().build(), trackDto);
	}

	@Test
	void testConvert () {
		final TrackDto trackDto = TrackEntityToDtoConverter.convert(TrackEntity.builder()
			.album(AlbumEntity.builder()
				.name("albumName")
				.tracks(new HashSet<>(Arrays.asList(
					TrackEntity.builder().name("").build(),
					TrackEntity.builder().name("").build())))
				.build())
			.artists(new HashSet<>(Arrays.asList(ArtistEntity.builder().name("artist2").build(),
				ArtistEntity.builder().name("artist1").build())))
			.availableMarkets(new HashSet<>(Arrays.asList(MarketEntity.builder().code(CountryCode.US).build(),
				MarketEntity.builder().code(CountryCode.BR).build())))
			.discNumber(1)
			.duration(1000)
			.explicit(false)
			.id("trackId")
			.name("trackName")
			.restriction("restriction")
			.trackNumber(2)
			.build());

		final TrackDto expected = TrackDto.builder()
			.album(AlbumDto.builder().name("albumName").build())
			.artists(Arrays.asList(ArtistDto.builder().name("artist1").build(),
				ArtistDto.builder().name("artist2").build()))
			.availableMarkets(Arrays.asList(CountryCode.BR, CountryCode.US))
			.discNumber(1)
			.duration(1000)
			.explicit(false)
			.id("trackId")
			.name("trackName")
			.restriction("restriction")
			.trackNumber(2)
			.build();
		assertEquals(expected, trackDto);
	}

	@Test
	void testConvertWithoutAlbum () {
		final TrackDto trackDto = TrackEntityToDtoConverter.convertWithoutAlbum(TrackEntity.builder()
			.album(AlbumEntity.builder().name("albumName").build())
			.artists(new HashSet<>(Arrays.asList(ArtistEntity.builder().name("artist1").build(),
				ArtistEntity.builder().name("artist2").build())))
			.availableMarkets(new HashSet<>(Arrays.asList(MarketEntity.builder().code(CountryCode.BR).build(),
				MarketEntity.builder().code(CountryCode.US).build())))
			.discNumber(1)
			.duration(1000)
			.explicit(false)
			.id("trackId")
			.name("trackName")
			.restriction("restriction")
			.trackNumber(2)
			.build());

		final TrackDto expected = TrackDto.builder()
			.artists(Arrays.asList(ArtistDto.builder().name("artist1").build(),
				ArtistDto.builder().name("artist2").build()))
			.availableMarkets(Arrays.asList(CountryCode.BR, CountryCode.US))
			.discNumber(1)
			.duration(1000)
			.explicit(false)
			.id("trackId")
			.name("trackName")
			.restriction("restriction")
			.trackNumber(2)
			.build();
		assertEquals(expected, trackDto);
	}
}
