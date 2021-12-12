package br.daniloikuta.spotifyavailability.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.envers.Audited;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "Genre")
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@Audited
public class GenreEntity {
	@Id
	@Column(nullable = false, length = 100)
	private String genre;
}
