package br.daniloikuta.spotifyavailability.business;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.daniloikuta.spotifyavailability.converter.todto.AlbumEntityToDtoConverter;
import br.daniloikuta.spotifyavailability.dto.AlbumAvailabilityResponseDto;
import br.daniloikuta.spotifyavailability.entity.AlbumEntity;
import br.daniloikuta.spotifyavailability.entity.ArtistEntity;
import br.daniloikuta.spotifyavailability.entity.TrackEntity;
import br.daniloikuta.spotifyavailability.repository.AlbumRepository;
import br.daniloikuta.spotifyavailability.repository.ArtistRepository;
import br.daniloikuta.spotifyavailability.repository.TrackRepository;
import br.daniloikuta.spotifyavailability.service.SpotifyService;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class AlbumAvailabilityBusiness {
	@Autowired
	private SpotifyService spotifyService;

	@Autowired
	private AlbumRepository albumRepository;

	@Autowired
	private TrackRepository trackRepository;

	@Autowired
	private ArtistRepository artistRepository;

	public AlbumAvailabilityResponseDto getAlbumAvailabilities (final List<String> ids) {
		final List<AlbumEntity> albums = spotifyService.getAlbums(ids);
		log.debug(albums.toString());

		if (albums.isEmpty()) {
			return AlbumAvailabilityResponseDto.builder().build();
		}

		final Set<TrackEntity> tracks =
			albums.stream().map(this::setAlbumToTracks).flatMap(Set::stream).collect(Collectors.toSet());
		final List<ArtistEntity> artists =
			tracks.stream().map(TrackEntity::getArtists).flatMap(Set::stream).distinct().toList();

		artistRepository.saveAll(artists);

		albums.forEach(album -> album.setTracks(null));
		final List<AlbumEntity> savedAlbums = albumRepository.saveAll(albums);
		log.debug(savedAlbums.toString());

		trackRepository.saveAll(tracks);

		// TODO: get alternatives?
		// TODO: return tracks?

		return AlbumAvailabilityResponseDto.builder()
			.albums(savedAlbums.stream().map(AlbumEntityToDtoConverter::convert).toList())
			.build();
	}

	private Set<TrackEntity> setAlbumToTracks (final AlbumEntity album) {
		final Set<TrackEntity> tracks = album.getTracks();
		final AlbumEntity albumWithoutTracks = album.toBuilder().tracks(null).build();
		tracks.forEach(track -> track.setAlbum(albumWithoutTracks));
		return tracks;
	}
}