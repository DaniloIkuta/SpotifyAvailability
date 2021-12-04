package br.daniloikuta.spotifyavailability.business;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.history.Revisions;
import org.springframework.stereotype.Component;

import br.daniloikuta.spotifyavailability.converter.todto.AlbumEntityToDtoConverter;
import br.daniloikuta.spotifyavailability.dto.AlbumDto;
import br.daniloikuta.spotifyavailability.dto.RevisionDto;
import br.daniloikuta.spotifyavailability.dto.RevisionMetadataDto;
import br.daniloikuta.spotifyavailability.entity.AlbumEntity;
import br.daniloikuta.spotifyavailability.repository.AlbumRepository;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class AlbumBusiness {
	@Autowired
	private AlbumRepository albumRepository;

	public List<AlbumDto> findAlbums (final List<String> ids) {
		final List<AlbumEntity> albumEntities = albumRepository.findAllById(ids);

		return albumEntities.stream().map(AlbumEntityToDtoConverter::convert).toList();
	}

	public List<RevisionDto<AlbumDto>> getRevisionHistory (final String id) {
		final Revisions<Integer, AlbumEntity> revisions = albumRepository.findRevisions(id);

		if (revisions.isEmpty()) {
			throw new EntityNotFoundException("Album not found with ID: " + id);
		}

		return revisions.stream().map(revision -> {
			final AlbumEntity entity = revision.getEntity();
			final AlbumDto dto = AlbumEntityToDtoConverter.convertWithoutTracks(entity);
			final RevisionMetadataDto metadata = RevisionMetadataDto.builder()
				.id(revision.getRequiredRevisionNumber())
				.timestamp(LocalDateTime.ofInstant(revision.getMetadata().getRequiredRevisionInstant(),
					ZoneId.systemDefault()))
				.type(revision.getMetadata().getRevisionType().toString())
				.build();
			return RevisionDto.<AlbumDto>builder().entry(dto).metadata(metadata).build();
		}).toList();
	}
}
