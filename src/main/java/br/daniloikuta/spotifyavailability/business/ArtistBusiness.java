package br.daniloikuta.spotifyavailability.business;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.daniloikuta.spotifyavailability.converter.todto.ArtistEntityToDtoConverter;
import br.daniloikuta.spotifyavailability.dto.ArtistDto;
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
}
