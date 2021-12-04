package br.daniloikuta.spotifyavailability.business;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.history.Revisions;
import org.springframework.stereotype.Component;

import br.daniloikuta.spotifyavailability.converter.todto.TrackEntityToDtoConverter;
import br.daniloikuta.spotifyavailability.dto.RevisionDto;
import br.daniloikuta.spotifyavailability.dto.RevisionMetadataDto;
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

	public List<RevisionDto<TrackDto>> getRevisionHistory (final String id) {
		final Revisions<Integer, TrackEntity> revisions = trackRepository.findRevisions(id);

		if (revisions.isEmpty()) {
			throw new EntityNotFoundException("Track not found with ID: " + id);
		}

		return revisions.stream().map(revision -> {
			final TrackEntity entity = revision.getEntity();
			final TrackDto dto = TrackEntityToDtoConverter.convert(entity);
			final RevisionMetadataDto metadata = RevisionMetadataDto.builder()
				.id(revision.getRequiredRevisionNumber())
				.timestamp(LocalDateTime.ofInstant(revision.getMetadata().getRequiredRevisionInstant(),
					ZoneId.systemDefault()))
				.type(revision.getMetadata().getRevisionType().toString())
				.build();
			return RevisionDto.<TrackDto>builder().entry(dto).metadata(metadata).build();
		}).toList();
	}
}
