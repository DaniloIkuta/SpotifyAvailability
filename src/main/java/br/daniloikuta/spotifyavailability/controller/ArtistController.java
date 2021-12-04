package br.daniloikuta.spotifyavailability.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.daniloikuta.spotifyavailability.business.ArtistBusiness;
import br.daniloikuta.spotifyavailability.dto.ArtistDto;
import br.daniloikuta.spotifyavailability.dto.RevisionDto;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/artist")
@Slf4j
@Validated
public class ArtistController {
	@Autowired
	private ArtistBusiness artistBusiness;

	@GetMapping
	public ResponseEntity<List<ArtistDto>> findArtists (@RequestParam(name = "ids") final List<String> ids) {
		final List<ArtistDto> artists = artistBusiness.findArtists(ids);

		log.debug(artists.toString());

		return ResponseEntity.ok(artists);
	}

	@GetMapping("/revisionHistory")
	public ResponseEntity<List<RevisionDto<ArtistDto>>>
		getRevisionHistory (@RequestParam(name = "id") final String id) {
		final List<RevisionDto<ArtistDto>> revisionHistory = artistBusiness.getRevisionHistory(id);

		log.debug(revisionHistory.toString());

		return ResponseEntity.ok(revisionHistory);
	}
}
