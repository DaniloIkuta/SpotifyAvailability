package br.daniloikuta.spotifyavailability.business;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.daniloikuta.spotifyavailability.converter.todto.AlbumEntityToDtoConverter;
import br.daniloikuta.spotifyavailability.dto.AlbumDto;
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
}
