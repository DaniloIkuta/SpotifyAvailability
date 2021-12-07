package br.daniloikuta.spotifyavailability.entity;

import java.time.LocalDate;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.envers.Audited;

import br.daniloikuta.spotifyavailability.enums.AlbumType;
import br.daniloikuta.spotifyavailability.enums.ReleaseDatePrecision;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Table(name = "Album")
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder(toBuilder = true)
@Audited
public class AlbumEntity {
	@Id
	@Column(nullable = false, unique = true, length = 64)
	private String id;

	@Column(nullable = false)
	private String name;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false, length = 11)
	private AlbumType type;

	@Column
	private Integer trackCount;

	@Column(length = 10)
	private String releaseDate;

	@Enumerated(EnumType.STRING)
	@Column
	private ReleaseDatePrecision releaseDatePrecision;

	@Column
	private String copyrights;

	@ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	@JoinTable(name = "AlbumAvailability",
		joinColumns = @JoinColumn(name = "albumId"),
		inverseJoinColumns = @JoinColumn(name = "marketCode"))
	private Set<MarketEntity> availableMarkets;

	@Column(length = 10)
	private String restriction;

	@ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	@JoinTable(name = "AlbumGenre",
		joinColumns = @JoinColumn(name = "albumId"),
		inverseJoinColumns = @JoinColumn(name = "genreId"))
	private Set<GenreEntity> genres;

	@ManyToMany(mappedBy = "albums", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	private Set<ArtistEntity> artists;

	@EqualsAndHashCode.Exclude
	@ToString.Exclude
	@OneToMany(mappedBy = "album")
	private Set<TrackEntity> tracks;

	@Column(nullable = false)
	private LocalDate lastUpdated;
}
