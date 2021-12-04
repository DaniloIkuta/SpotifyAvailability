package br.daniloikuta.spotifyavailability.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.history.RevisionRepository;
import org.springframework.stereotype.Repository;

import br.daniloikuta.spotifyavailability.entity.AlbumEntity;

@Repository
public interface AlbumRepository extends
	JpaRepository<AlbumEntity, String>,
	RevisionRepository<AlbumEntity, String, Integer> {}
