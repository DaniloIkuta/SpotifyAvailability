package br.daniloikuta.spotifyavailability.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.history.RevisionRepository;
import org.springframework.stereotype.Repository;

import br.daniloikuta.spotifyavailability.entity.MarketEntity;

@Repository
public interface MarketRepository extends
	JpaRepository<MarketEntity, Long>,
	RevisionRepository<MarketEntity, Long, Long> {

}
