package br.daniloikuta.spotifyavailability.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.history.RevisionRepository;
import org.springframework.stereotype.Repository;

import br.daniloikuta.spotifyavailability.entity.GenreEntity;

@Repository
public interface GenreRepository extends
	JpaRepository<GenreEntity, String>,
	RevisionRepository<GenreEntity, String, Integer> {

}
