package br.daniloikuta.spotifyavailability.business;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.neovisionaries.i18n.CountryCode;

import br.daniloikuta.spotifyavailability.dto.AlbumDto;
import br.daniloikuta.spotifyavailability.dto.ArtistAlbumsAvailabilityResponseDto;
import br.daniloikuta.spotifyavailability.dto.ArtistDto;
import br.daniloikuta.spotifyavailability.entity.AlbumEntity;
import br.daniloikuta.spotifyavailability.entity.ArtistEntity;
import br.daniloikuta.spotifyavailability.entity.MarketEntity;
import br.daniloikuta.spotifyavailability.enums.AlbumType;
import br.daniloikuta.spotifyavailability.repository.AlbumRepository;
import br.daniloikuta.spotifyavailability.repository.ArtistRepository;
import br.daniloikuta.spotifyavailability.service.SpotifyService;

@ExtendWith(MockitoExtension.class)
public class ArtistAlbumsAvailabilityBusinessTest {
	@InjectMocks
	private ArtistAlbumsAvailabilityBusiness artistAlbumsAvailabilityBusiness;

	@Mock
	private SpotifyService spotifyService;

	@Mock
	private AlbumRepository albumRepository;

	@Mock
	private ArtistRepository artistRepository;

	@Test
	void testGetArtistAlbumsAvailabilitiesNoneFound () {
		when(spotifyService.getArtistAlbums("artistId")).thenReturn(new ArrayList<>());

		final ArtistAlbumsAvailabilityResponseDto artistAlbumsAvailabilities =
			artistAlbumsAvailabilityBusiness.getArtistAlbumsAvailabilities("artistId");

		assertEquals(ArtistAlbumsAvailabilityResponseDto.builder().build(), artistAlbumsAvailabilities);
		verifyNoInteractions(albumRepository, artistRepository);
	}

	@Test
	void testGetArtistAlbumsAvailabilities () {
		setupTestGetArtistAlbumsAvailabilities();

		final ArtistAlbumsAvailabilityResponseDto artistAlbumsAvailabilities =
			artistAlbumsAvailabilityBusiness.getArtistAlbumsAvailabilities("artistId");

		final ArtistDto artistDto = ArtistDto.builder().id("artistId").name("artistName").build();
		final ArtistDto otherArtistDto = ArtistDto.builder().id("otherArtistId").name("otherArtistName").build();
		final Set<CountryCode> countryCodes = new HashSet<>(Arrays.asList(CountryCode.BR, CountryCode.US));
		final List<AlbumDto> albumDtos = Arrays.asList(
			AlbumDto.builder()
				.artists(new HashSet<>(Arrays.asList(artistDto)))
				.availableMarkets(countryCodes)
				.id("albumId1")
				.name("album1")
				.restriction("restriction")
				.type(AlbumType.ALBUM)
				.build(),
			AlbumDto.builder()
				.artists(new HashSet<>(Arrays.asList(artistDto, otherArtistDto)))
				.availableMarkets(countryCodes)
				.id("albumId2")
				.name("album2")
				.restriction("restriction")
				.type(AlbumType.ALBUM)
				.build());
		final ArtistAlbumsAvailabilityResponseDto expected = ArtistAlbumsAvailabilityResponseDto.builder()
			.artist(artistDto)
			.albums(albumDtos)
			.build();

		assertEquals(expected, artistAlbumsAvailabilities);
	}

	private void setupTestGetArtistAlbumsAvailabilities () {
		final ArtistEntity artistEntity = ArtistEntity.builder().id("artistId").name("artistName").build();
		final Set<MarketEntity> marketEntities =
			new HashSet<>(Arrays.asList(MarketEntity.builder().code(CountryCode.BR).build(),
				MarketEntity.builder().code(CountryCode.US).build()));

		final AlbumEntity albumEntity1 = AlbumEntity.builder()
			.artists(new HashSet<>(Arrays.asList(artistEntity)))
			.availableMarkets(marketEntities)
			.id("albumId1")
			.name("album1")
			.restriction("restriction")
			.type(AlbumType.ALBUM)
			.build();

		final ArtistEntity otherArtistEntity =
			ArtistEntity.builder().id("otherArtistId").name("otherArtistName").build();
		final List<ArtistEntity> allArtistEntities = Arrays.asList(artistEntity, otherArtistEntity);
		final AlbumEntity albumEntity2 = AlbumEntity.builder()
			.artists(new HashSet<>(allArtistEntities))
			.availableMarkets(marketEntities)
			.id("albumId2")
			.name("album2")
			.restriction("restriction")
			.type(AlbumType.ALBUM)
			.build();

		final List<AlbumEntity> artistAlbumEntities = Arrays.asList(albumEntity1, albumEntity2);
		when(spotifyService.getArtistAlbums("artistId")).thenReturn(artistAlbumEntities);
		when(artistRepository.saveAll(allArtistEntities)).thenReturn(allArtistEntities);
		when(albumRepository.saveAll(artistAlbumEntities)).thenReturn(artistAlbumEntities);
	}
}
