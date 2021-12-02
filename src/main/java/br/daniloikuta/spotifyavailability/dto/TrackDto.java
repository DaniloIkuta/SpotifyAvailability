package br.daniloikuta.spotifyavailability.dto;

import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.neovisionaries.i18n.CountryCode;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(value = Include.NON_EMPTY)
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class TrackDto {
	private String id;
	private String name;
	private Set<ArtistDto> artists;
	private AlbumDto album;
	private Set<CountryCode> availableMarkets;
	private Integer discNumber;
	private Integer trackNumber;
	private Integer duration;
	private Boolean explicit;
	private String restriction;
}