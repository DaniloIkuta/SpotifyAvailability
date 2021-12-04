package br.daniloikuta.spotifyavailability.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.daniloikuta.spotifyavailability.business.AlbumBusiness;
import br.daniloikuta.spotifyavailability.dto.AlbumDto;
import br.daniloikuta.spotifyavailability.dto.RevisionDto;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/album")
@Slf4j
@Validated
public class AlbumController {
	@Autowired
	private AlbumBusiness albumBusiness;

	@GetMapping
	public ResponseEntity<List<AlbumDto>> findAlbums (@RequestParam(name = "ids") final List<String> ids) {
		final List<AlbumDto> albums = albumBusiness.findAlbums(ids);

		log.debug(albums.toString());

		return ResponseEntity.ok(albums);
	}

	@GetMapping("/revisionHistory")
	public ResponseEntity<List<RevisionDto<AlbumDto>>> getRevisionHistory (@RequestParam(name = "id") final String id) {
		final List<RevisionDto<AlbumDto>> revisionHistory = albumBusiness.getRevisionHistory(id);

		log.debug(revisionHistory.toString());

		return ResponseEntity.ok(revisionHistory);
	}
}
