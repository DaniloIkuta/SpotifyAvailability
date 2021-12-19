package br.daniloikuta.spotifyavailability.converter.todto;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashSet;

import org.junit.jupiter.api.Test;

import com.neovisionaries.i18n.CountryCode;

import br.daniloikuta.spotifyavailability.dto.AlbumDto;
import br.daniloikuta.spotifyavailability.dto.ArtistDto;
import br.daniloikuta.spotifyavailability.dto.TrackDto;
import br.daniloikuta.spotifyavailability.entity.AlbumEntity;
import br.daniloikuta.spotifyavailability.entity.ArtistEntity;
import br.daniloikuta.spotifyavailability.entity.GenreEntity;
import br.daniloikuta.spotifyavailability.entity.MarketEntity;
import br.daniloikuta.spotifyavailability.entity.TrackEntity;
import br.daniloikuta.spotifyavailability.enums.AlbumType;
import br.daniloikuta.spotifyavailability.enums.ReleaseDatePrecision;

class AlbumEntityToDtoConverterTest {
	@Test
	void testConvertNull () {
		final AlbumDto albumDto = AlbumEntityToDtoConverter.convert(null);

		assertNull(albumDto);
	}

	@Test
	void testConvertNullProperties () {
		final AlbumDto albumDto = AlbumEntityToDtoConverter.convert(AlbumEntity.builder().build());

		assertEquals(AlbumDto.builder().build(), albumDto);
	}

	@Test
	void testConvert () {
		final AlbumDto albumDto = AlbumEntityToDtoConverter.convert(AlbumEntity.builder()
			.artists(new HashSet<>(Arrays.asList(ArtistEntity.builder().name("artist2").build(),
				ArtistEntity.builder().name("artist1").build())))
			.availableMarkets(new HashSet<>(Arrays.asList(MarketEntity.builder().code(CountryCode.US).build(),
				MarketEntity.builder().code(CountryCode.BR).build())))
			.copyrights("copyrights")
			.genres(new HashSet<>(Arrays.asList(GenreEntity.builder().genre("genre2").build(),
				GenreEntity.builder().genre("genre1").build())))
			.id("albumId")
			.name("albumName")
			.releaseDate("2021-01-01")
			.releaseDatePrecision(ReleaseDatePrecision.DAY)
			.restriction("restriction")
			.trackCount(2)
			.tracks(new HashSet<>(Arrays.asList(
				TrackEntity.builder()
					.name("track2")
					.discNumber(1)
					.trackNumber(2)
					.album(AlbumEntity.builder().name("").build())
					.build(),
				TrackEntity.builder()
					.name("track3")
					.discNumber(2)
					.trackNumber(1)
					.album(AlbumEntity.builder().name("").build())
					.build(),
				TrackEntity.builder()
					.name("track1")
					.discNumber(1)
					.trackNumber(1)
					.album(AlbumEntity.builder().name("").build())
					.build())))
			.type(AlbumType.ALBUM)
			.lastUpdated(LocalDate.of(2021, 12, 6))
			.build());

		final AlbumDto expected = AlbumDto.builder()
			.artists(Arrays.asList(ArtistDto.builder().name("artist1").build(),
				ArtistDto.builder().name("artist2").build()))
			.availableMarkets(Arrays.asList(CountryCode.BR, CountryCode.US))
			.copyrights("copyrights")
			.genres(Arrays.asList("genre1", "genre2"))
			.id("albumId")
			.name("albumName")
			.releaseDate("2021-01-01")
			.releaseDatePrecision(ReleaseDatePrecision.DAY)
			.restriction("restriction")
			.trackCount(2)
			.tracks(
				Arrays.asList(TrackDto.builder().name("track1").discNumber(1).trackNumber(1).build(),
					TrackDto.builder().name("track2").discNumber(1).trackNumber(2).build(),
					TrackDto.builder().name("track3").discNumber(2).trackNumber(1).build()))
			.type(AlbumType.ALBUM)
			.lastUpdated(LocalDate.of(2021, 12, 6))
			.build();
		assertEquals(expected, albumDto);
	}

	@Test
	void testConverWithoutTracks () {
		final AlbumDto albumDto = AlbumEntityToDtoConverter.convertWithoutTracks(AlbumEntity.builder()
			.artists(new HashSet<>(Arrays.asList(ArtistEntity.builder().name("artist1").build(),
				ArtistEntity.builder().name("artist2").build())))
			.availableMarkets(new HashSet<>(Arrays.asList(MarketEntity.builder().code(CountryCode.BR).build(),
				MarketEntity.builder().code(CountryCode.US).build())))
			.copyrights("copyrights")
			.genres(new HashSet<>(Arrays.asList(GenreEntity.builder().genre("genre1").build(),
				GenreEntity.builder().genre("genre2").build())))
			.id("albumId")
			.name("albumName")
			.releaseDate("2021-01-01")
			.releaseDatePrecision(ReleaseDatePrecision.DAY)
			.restriction("restriction")
			.trackCount(2)
			.tracks(new HashSet<>(Arrays.asList(
				TrackEntity.builder().name("track1").build(),
				TrackEntity.builder().name("track2").build())))
			.type(AlbumType.ALBUM)
			.lastUpdated(LocalDate.of(2021, 12, 6))
			.build());

		final AlbumDto expected = AlbumDto.builder()
			.artists(Arrays.asList(ArtistDto.builder().name("artist1").build(),
				ArtistDto.builder().name("artist2").build()))
			.availableMarkets(Arrays.asList(CountryCode.BR, CountryCode.US))
			.copyrights("copyrights")
			.genres(Arrays.asList("genre1", "genre2"))
			.id("albumId")
			.name("albumName")
			.releaseDate("2021-01-01")
			.releaseDatePrecision(ReleaseDatePrecision.DAY)
			.restriction("restriction")
			.trackCount(2)
			.type(AlbumType.ALBUM)
			.lastUpdated(LocalDate.of(2021, 12, 6))
			.build();
		assertEquals(expected, albumDto);
	}
}
