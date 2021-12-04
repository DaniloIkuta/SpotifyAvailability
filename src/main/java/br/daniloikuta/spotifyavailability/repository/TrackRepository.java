package br.daniloikuta.spotifyavailability.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.history.RevisionRepository;
import org.springframework.stereotype.Repository;

import br.daniloikuta.spotifyavailability.entity.TrackEntity;

@Repository
public interface TrackRepository extends
	JpaRepository<TrackEntity, String>,
	RevisionRepository<TrackEntity, String, Long> {

}
