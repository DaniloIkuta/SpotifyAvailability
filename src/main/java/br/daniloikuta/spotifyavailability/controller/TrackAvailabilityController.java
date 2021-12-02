package br.daniloikuta.spotifyavailability.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.daniloikuta.spotifyavailability.business.TrackAvailabilityBusiness;
import br.daniloikuta.spotifyavailability.dto.TrackAvailabilityResponseDto;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/trackAvailability")
@Slf4j
@Validated
public class TrackAvailabilityController {
	@Autowired
	private TrackAvailabilityBusiness trackAvailabilityBusiness;

	@GetMapping
	public ResponseEntity<TrackAvailabilityResponseDto>
		getAvailabilities (@RequestParam(name = "ids") final List<String> ids) {
		final TrackAvailabilityResponseDto trackAvailabilities = trackAvailabilityBusiness.getTrackAvailabilities(ids);

		return ResponseEntity.ok(trackAvailabilities);
	}

}
