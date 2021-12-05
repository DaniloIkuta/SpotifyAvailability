package br.daniloikuta.spotifyavailability.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.history.RevisionRepository;
import org.springframework.stereotype.Repository;

import br.daniloikuta.spotifyavailability.entity.AlbumEntity;

@Repository
public interface AlbumRepository extends
	JpaRepository<AlbumEntity, String>,
	RevisionRepository<AlbumEntity, String, Integer> {

	@Query("SELECT a.id FROM AlbumEntity a LEFT JOIN TrackEntity t ON a.id = t.album.id WHERE t.album.id = NULL")
	List<String> findAlbumIdsWithoutTracks ();
}
