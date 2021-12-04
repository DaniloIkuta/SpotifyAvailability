package br.daniloikuta.spotifyavailability.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.history.RevisionRepository;
import org.springframework.stereotype.Repository;

import br.daniloikuta.spotifyavailability.entity.ArtistEntity;

@Repository
public interface ArtistRepository extends
	JpaRepository<ArtistEntity, String>,
	RevisionRepository<ArtistEntity, String, Long> {

}
