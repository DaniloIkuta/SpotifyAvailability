package br.daniloikuta.spotifyavailability.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.daniloikuta.spotifyavailability.business.TrackBusiness;
import br.daniloikuta.spotifyavailability.dto.TrackDto;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/track")
@Slf4j
@Validated
public class TrackController {
	@Autowired
	private TrackBusiness trackBusiness;

	@GetMapping
	public ResponseEntity<List<TrackDto>> findTracks (@RequestParam(name = "ids") final List<String> ids) {
		final List<TrackDto> tracks = trackBusiness.findTracks(ids);

		log.debug(tracks.toString());

		return ResponseEntity.ok(tracks);
	}
}
