package br.daniloikuta.spotifyavailability.dto;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

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
public class RevisionMetadataDto {
	private Integer id;
	private String type;
	private LocalDateTime timestamp;
}
