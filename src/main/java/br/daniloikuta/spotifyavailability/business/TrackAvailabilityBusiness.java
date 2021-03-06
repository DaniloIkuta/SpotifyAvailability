package br.daniloikuta.spotifyavailability.business;

import java.time.Clock;
import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.daniloikuta.spotifyavailability.converter.todto.TrackEntityToDtoConverter;
import br.daniloikuta.spotifyavailability.dto.TrackAvailabilityResponseDto;
import br.daniloikuta.spotifyavailability.entity.AlbumEntity;
import br.daniloikuta.spotifyavailability.entity.TrackEntity;
import br.daniloikuta.spotifyavailability.repository.AlbumRepository;
import br.daniloikuta.spotifyavailability.repository.TrackRepository;
import br.daniloikuta.spotifyavailability.service.SpotifyService;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class TrackAvailabilityBusiness {
	@Autowired
	private SpotifyService spotifyService;

	@Autowired
	private AlbumRepository albumRepository;

	@Autowired
	private TrackRepository trackRepository;

	@Autowired
	private Clock clock;

	public TrackAvailabilityResponseDto getTrackAvailabilities (final List<String> ids) {
		final List<TrackEntity> tracks = spotifyService.getTracks(ids);
		log.debug(tracks.toString());

		if (tracks.isEmpty()) {
			return TrackAvailabilityResponseDto.builder().build();
		}

		final List<AlbumEntity> albums = tracks.stream().map(TrackEntity::getAlbum).toList();

		final LocalDate now = LocalDate.now(clock);
		albums.forEach(album -> album.setLastUpdated(now));

		albumRepository.saveAll(albums);

		final List<TrackEntity> savedTracks = trackRepository.saveAll(tracks);

		// TODO: get alternatives?

		return TrackAvailabilityResponseDto.builder()
			.tracks(savedTracks.stream().map(TrackEntityToDtoConverter::convert).toList())
			.build();
	}
}
