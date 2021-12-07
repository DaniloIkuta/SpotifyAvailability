package br.daniloikuta.spotifyavailability.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.daniloikuta.spotifyavailability.business.AlbumAvailabilityBusiness;
import br.daniloikuta.spotifyavailability.dto.AlbumAvailabilityResponseDto;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/albumAvailability")
@Slf4j
@Validated
public class AlbumAvailabilityController {
	@Autowired
	private AlbumAvailabilityBusiness albumAvailabilityBusiness;

	@GetMapping
	public ResponseEntity<AlbumAvailabilityResponseDto>
		getAvailabilities (@RequestParam(name = "ids") final List<String> ids) {
		final AlbumAvailabilityResponseDto albumAvailabilities = albumAvailabilityBusiness.getAlbumAvailabilities(ids);

		log.debug(albumAvailabilities.toString());

		return ResponseEntity.ok(albumAvailabilities);
	}

	@PostMapping("/missingTracks")
	public ResponseEntity<Void> fetchMissingTracks () {
		albumAvailabilityBusiness.fetchMissingTracks();

		return ResponseEntity.ok(null);
	}

	@PostMapping("/refresh")
	public ResponseEntity<Void> refreshAvailabilities () {
		albumAvailabilityBusiness.refreshAvailabilities();

		return ResponseEntity.ok(null);
	}
}
