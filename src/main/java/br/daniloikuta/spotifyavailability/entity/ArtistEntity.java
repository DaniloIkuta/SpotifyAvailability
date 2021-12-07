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
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Table(name = "Artist")
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder(toBuilder = true)
@Audited
public class ArtistEntity {

	@Id
	@Column(nullable = false, unique = true, length = 64)
	private String id;

	@Column(nullable = false)
	private String name;

	@EqualsAndHashCode.Exclude
	@ToString.Exclude
	@ManyToMany
	@JoinTable(name = "ArtistAlbum")
	private Set<AlbumEntity> albums;

	@EqualsAndHashCode.Exclude
	@ToString.Exclude
	@ManyToMany
	@JoinTable(name = "ArtistTrack")
	private Set<TrackEntity> tracks;
}
