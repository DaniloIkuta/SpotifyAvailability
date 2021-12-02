package br.daniloikuta.spotifyavailability.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.history.RevisionRepository;
import org.springframework.stereotype.Repository;

import br.daniloikuta.spotifyavailability.entity.CopyrightEntity;

@Repository
public interface CopyrightRepository extends
	JpaRepository<CopyrightEntity, Long>,
	RevisionRepository<CopyrightEntity, Long, Long> {

}
