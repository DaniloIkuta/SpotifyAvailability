package br.daniloikuta.spotifyavailability.business;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.daniloikuta.spotifyavailability.converter.todto.TrackEntityToDtoConverter;
import br.daniloikuta.spotifyavailability.dto.TrackDto;
import br.daniloikuta.spotifyavailability.entity.TrackEntity;
import br.daniloikuta.spotifyavailability.repository.TrackRepository;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class TrackBusiness {
	@Autowired
	private TrackRepository trackRepository;

	public List<TrackDto> findTracks (final List<String> ids) {
		final List<TrackEntity> tracks = trackRepository.findAllById(ids);

		return tracks.stream().map(TrackEntityToDtoConverter::convert).toList();
	}
}
