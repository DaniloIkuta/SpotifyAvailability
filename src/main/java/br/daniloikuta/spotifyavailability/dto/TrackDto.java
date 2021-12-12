package br.daniloikuta.spotifyavailability.dto;

import java.util.List;

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
@Builder(toBuilder = true)
public class TrackDto {
	private String id;
	private String name;
	private List<ArtistDto> artists;
	private AlbumDto album;
	private List<CountryCode> availableMarkets;
	private Integer discNumber;
	private Integer trackNumber;
	private Integer duration;
	private Boolean explicit;
	private String restriction;
}
