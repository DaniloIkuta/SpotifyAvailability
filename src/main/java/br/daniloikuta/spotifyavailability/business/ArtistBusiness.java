package br.daniloikuta.spotifyavailability.business;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.history.Revisions;
import org.springframework.stereotype.Component;

import br.daniloikuta.spotifyavailability.converter.todto.ArtistEntityToDtoConverter;
import br.daniloikuta.spotifyavailability.dto.ArtistDto;
import br.daniloikuta.spotifyavailability.dto.RevisionDto;
import br.daniloikuta.spotifyavailability.dto.RevisionMetadataDto;
import br.daniloikuta.spotifyavailability.entity.ArtistEntity;
import br.daniloikuta.spotifyavailability.repository.ArtistRepository;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class ArtistBusiness {
	@Autowired
	private ArtistRepository artistRepository;

	public List<ArtistDto> findArtists (final List<String> ids) {
		final List<ArtistEntity> artists = artistRepository.findAllById(ids);
		return artists.stream().map(ArtistEntityToDtoConverter::convert).toList();
	}

	public List<RevisionDto<ArtistDto>> getRevisionHistory (final String id) {
		final Revisions<Integer, ArtistEntity> revisions = artistRepository.findRevisions(id);

		if (revisions.isEmpty()) {
			throw new EntityNotFoundException("Artist not found with ID: " + id);
		}

		return revisions.stream().map(revision -> {
			final ArtistEntity entity = revision.getEntity();
			final ArtistDto dto = ArtistEntityToDtoConverter.convert(entity);
			final RevisionMetadataDto metadata = RevisionMetadataDto.builder()
				.id(revision.getRequiredRevisionNumber())
				.timestamp(LocalDateTime.ofInstant(revision.getMetadata().getRequiredRevisionInstant(),
					ZoneId.systemDefault()))
				.type(revision.getMetadata().getRevisionType().toString())
				.build();
			return RevisionDto.<ArtistDto>builder().entry(dto).metadata(metadata).build();
		}).toList();
	}
}
