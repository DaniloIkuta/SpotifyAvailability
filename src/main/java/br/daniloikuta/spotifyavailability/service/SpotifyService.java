package br.daniloikuta.spotifyavailability.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import org.apache.hc.core5.http.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;

import br.daniloikuta.spotifyavailability.converter.toentity.AlbumSimplifiedToAlbumEntityConverter;
import br.daniloikuta.spotifyavailability.converter.toentity.AlbumToAlbumEntityConverter;
import br.daniloikuta.spotifyavailability.converter.toentity.TrackSimplifiedToTrackEntityConverter;
import br.daniloikuta.spotifyavailability.converter.toentity.TrackToTrackEntityConverter;
import br.daniloikuta.spotifyavailability.entity.AlbumEntity;
import br.daniloikuta.spotifyavailability.entity.TrackEntity;
import br.daniloikuta.spotifyavailability.exception.SpotifyClientException;
import lombok.extern.slf4j.Slf4j;
import se.michaelthelin.spotify.SpotifyApi;
import se.michaelthelin.spotify.exceptions.SpotifyWebApiException;
import se.michaelthelin.spotify.model_objects.specification.Album;
import se.michaelthelin.spotify.model_objects.specification.AlbumSimplified;
import se.michaelthelin.spotify.model_objects.specification.Paging;
import se.michaelthelin.spotify.model_objects.specification.Track;
import se.michaelthelin.spotify.model_objects.specification.TrackSimplified;

@Service
@Slf4j
public class SpotifyService {

	@Autowired
	private SpotifyApi spotifyApi;

	protected static Integer maxItemsPerPage = 50;
	protected static Integer maxAlbumIdsPerRequest = 20;
	protected static Integer maxTrackIdsPerRequest = 50;

	public List<AlbumEntity> getArtistAlbums (final String id) {
		try {
			Paging<AlbumSimplified> response =
				spotifyApi.getArtistsAlbums(id).limit(maxItemsPerPage).build().execute();
			log.debug(response.toString());

			final List<AlbumSimplified> albums =
				response.getItems() == null ? new ArrayList<>() : new ArrayList<>(Arrays.asList(response.getItems()));

			int page = 1;
			while (response.getNext() != null) {
				response = spotifyApi.getArtistsAlbums(id)
					.offset(maxItemsPerPage * page)
					.limit(maxItemsPerPage)
					.build()
					.execute();
				log.debug(response.toString());

				albums.addAll(Arrays.asList(response.getItems()));
				page++;
			}

			return albums.stream()
				.filter(Objects::nonNull)
				.map(AlbumSimplifiedToAlbumEntityConverter::convert)
				.toList();
		} catch (ParseException | SpotifyWebApiException | IOException e) {
			log.error("Error trying to get artist's albums", e);
			throw new SpotifyClientException(e);
		}
	}

	public List<AlbumEntity> getAlbums (final List<String> ids) {
		try {
			final List<Album> albums = new ArrayList<>();

			for (final List<String> partIds : Lists.partition(ids, maxAlbumIdsPerRequest)) {
				final String[] idsArray = partIds.toArray(new String[0]);
				final Album[] response = spotifyApi.getSeveralAlbums(idsArray).build().execute();
				log.debug(Arrays.toString(response));

				if (response != null) {
					albums.addAll(Arrays.asList(response));
				}
			}

			final List<AlbumEntity> albumEntities =
				albums.stream().filter(Objects::nonNull).map(AlbumToAlbumEntityConverter::convert).toList();

			for (final AlbumEntity album : albumEntities) {
				if (album.getTracks() != null && album.getTrackCount() > album.getTracks().size()) {
					album.getTracks().addAll(getAlbumOtherTracks(album));
				}
			}

			return albumEntities;
		} catch (ParseException | SpotifyWebApiException | IOException e) {
			log.error("Error trying to get albums", e);
			throw new SpotifyClientException(e);
		}
	}

	private List<TrackEntity>
		getAlbumOtherTracks (final AlbumEntity album) {
		try {
			Paging<TrackSimplified> response =
				spotifyApi.getAlbumsTracks(album.getId())
					.offset(maxItemsPerPage)
					.limit(maxItemsPerPage)
					.build()
					.execute();
			log.debug(response.toString());

			final List<TrackSimplified> otherTracks =
				response.getItems() == null ? new ArrayList<>() : new ArrayList<>(Arrays.asList(response.getItems()));

			int page = 2;
			while (response.getNext() != null) {
				response = spotifyApi.getAlbumsTracks(album.getId())
					.offset(maxItemsPerPage * page)
					.limit(maxItemsPerPage)
					.build()
					.execute();
				log.debug(response.toString());

				otherTracks.addAll(Arrays.asList(response.getItems()));
				page++;
			}

			return otherTracks.stream()
				.filter(Objects::nonNull)
				.map(TrackSimplifiedToTrackEntityConverter::convert)
				.toList();
		} catch (ParseException | SpotifyWebApiException | IOException e) {
			log.error("Error trying to get album tracks", e);
			throw new SpotifyClientException(e);
		}
	}

	public List<TrackEntity> getTracks (final List<String> ids) {
		try {
			final List<Track> tracks = new ArrayList<>();

			for (final List<String> partIds : Lists.partition(ids, maxTrackIdsPerRequest)) {
				final String[] idsArray = partIds.toArray(new String[0]);
				final Track[] response = spotifyApi.getSeveralTracks(idsArray).build().execute();
				log.debug(Arrays.toString(response));

				if (response != null) {
					tracks.addAll(Arrays.asList(response));
				}
			}

			return tracks.stream().filter(Objects::nonNull).map(TrackToTrackEntityConverter::convert).toList();
		} catch (ParseException | SpotifyWebApiException | IOException e) {
			log.error("Error trying to get tracks", e);
			throw new SpotifyClientException(e);
		}
	}
}
