package br.daniloikuta.spotifyavailability.dto;

import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.neovisionaries.i18n.CountryCode;

import br.daniloikuta.spotifyavailability.enums.AlbumType;
import br.daniloikuta.spotifyavailability.enums.ReleaseDatePrecision;
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
public class AlbumDto {
	private String id;
	private String name;
	private AlbumType type;
	private Integer trackCount;
	private String releaseDate;
	private ReleaseDatePrecision releaseDatePrecision;
	private Set<String> copyrights;
	private Set<CountryCode> availableMarkets;
	private String restriction;
	private Set<String> genres;
	private Set<ArtistDto> artists;
	private Set<TrackDto> tracks;
}
