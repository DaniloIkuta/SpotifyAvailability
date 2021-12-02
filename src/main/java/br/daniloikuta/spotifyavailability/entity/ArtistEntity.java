package br.daniloikuta.spotifyavailability.entity;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import org.hibernate.envers.Audited;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "Artist")
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@Audited
public class ArtistEntity {

	@Id
	@Column(nullable = false, unique = true, length = 64)
	private String id;

	@Column(nullable = false)
	private String name;

	@ManyToMany
	@JoinTable(name = "ArtistAlbum")
	private Set<AlbumEntity> albums;

	@ManyToMany
	@JoinTable(name = "ArtistTrack")
	private Set<TrackEntity> tracks;
}
