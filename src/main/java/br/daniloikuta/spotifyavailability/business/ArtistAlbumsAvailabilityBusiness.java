package br.daniloikuta.spotifyavailability.business;

import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.daniloikuta.spotifyavailability.converter.todto.AlbumEntityToDtoConverter;
import br.daniloikuta.spotifyavailability.converter.todto.ArtistEntityToDtoConverter;
import br.daniloikuta.spotifyavailability.dto.ArtistAlbumsAvailabilityResponseDto;
import br.daniloikuta.spotifyavailability.entity.AlbumEntity;
import br.daniloikuta.spotifyavailability.entity.ArtistEntity;
import br.daniloikuta.spotifyavailability.repository.AlbumRepository;
import br.daniloikuta.spotifyavailability.repository.ArtistRepository;
import br.daniloikuta.spotifyavailability.service.SpotifyService;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class ArtistAlbumsAvailabilityBusiness {
	@Autowired
	private SpotifyService spotifyService;

	@Autowired
	private AlbumRepository albumRepository;

	@Autowired
	private ArtistRepository artistRepository;

	public ArtistAlbumsAvailabilityResponseDto getArtistAlbumsAvailabilities (final String id) {
		final List<AlbumEntity> artistAlbums = spotifyService.getArtistAlbums(id);
		log.debug(artistAlbums.toString());

		if (artistAlbums.isEmpty()) {
			return ArtistAlbumsAvailabilityResponseDto.builder().build();
		}

		final List<ArtistEntity> artists =
			artistAlbums.stream().map(AlbumEntity::getArtists).flatMap(Set::stream).distinct().toList();

		final List<ArtistEntity> savedArtists = artistRepository.saveAll(artists);
		final ArtistEntity searchedArtist =
			savedArtists.stream().filter(artist -> artist.getId().equals(id)).findAny().orElse(null);

		final List<AlbumEntity> savedAlbums = albumRepository.saveAll(artistAlbums);

		return ArtistAlbumsAvailabilityResponseDto.builder()
			.artist(ArtistEntityToDtoConverter.convert(searchedArtist))
			.albums(savedAlbums.stream().map(AlbumEntityToDtoConverter::convert).toList())
			.build();
	}
}
