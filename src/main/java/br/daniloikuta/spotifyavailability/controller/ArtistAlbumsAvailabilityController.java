package br.daniloikuta.spotifyavailability.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.daniloikuta.spotifyavailability.business.ArtistAlbumsAvailabilityBusiness;
import br.daniloikuta.spotifyavailability.dto.ArtistAlbumsAvailabilityResponseDto;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/artistAlbumsAvailability")
@Slf4j
@Validated
public class ArtistAlbumsAvailabilityController {
	@Autowired
	private ArtistAlbumsAvailabilityBusiness albumsAvailabilityBusiness;

	@GetMapping
	public ResponseEntity<ArtistAlbumsAvailabilityResponseDto>
		getAvailability (@RequestParam(name = "id") final String id) {
		final ArtistAlbumsAvailabilityResponseDto artistAlbumsAvailabilities =
			albumsAvailabilityBusiness.getArtistAlbumsAvailabilities(id);

		log.debug(artistAlbumsAvailabilities.toString());

		return ResponseEntity.ok(artistAlbumsAvailabilities);
	}
}
