package br.daniloikuta.spotifyavailability.entity;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.envers.Audited;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "Track")
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder(toBuilder = true)
@Audited
public class TrackEntity {

	@Id
	@Column(nullable = false, unique = true, length = 64)
	private String id;

	@Column(nullable = false)
	private String name;

	@ManyToMany(cascade = CascadeType.ALL)
	@JoinTable(name = "ArtistTrack",
		joinColumns = @JoinColumn(name = "trackId"),
		inverseJoinColumns = @JoinColumn(name = "artistId"))
	private Set<ArtistEntity> artists;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "albumId", nullable = false)
	private AlbumEntity album;

	@ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	@JoinTable(name = "TrackAvailability",
		joinColumns = @JoinColumn(name = "trackId"),
		inverseJoinColumns = @JoinColumn(name = "marketCode"))
	private Set<MarketEntity> availableMarkets;

	@Column(nullable = false)
	private Integer discNumber;

	@Column(nullable = false)
	private Integer trackNumber;

	@Column(nullable = false)
	private Integer duration;

	@Column(nullable = false)
	private Boolean explicit;

	@Column(length = 10)
	private String restriction;
}
