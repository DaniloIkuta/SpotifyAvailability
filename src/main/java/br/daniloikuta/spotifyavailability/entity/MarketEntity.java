package br.daniloikuta.spotifyavailability.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.envers.Audited;

import com.neovisionaries.i18n.CountryCode;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "Market")
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@Audited
public class MarketEntity {
	@Id
	@Enumerated(EnumType.STRING)
	@Column(nullable = false, unique = true, length = 2)
	private CountryCode code;
}
