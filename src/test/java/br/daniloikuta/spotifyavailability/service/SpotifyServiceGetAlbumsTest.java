package br.daniloikuta.spotifyavailability.service;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.apache.hc.core5.http.ParseException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import br.daniloikuta.spotifyavailability.entity.AlbumEntity;
import br.daniloikuta.spotifyavailability.entity.TrackEntity;
import se.michaelthelin.spotify.SpotifyApi;
import se.michaelthelin.spotify.exceptions.SpotifyWebApiException;
import se.michaelthelin.spotify.model_objects.specification.Album;
import se.michaelthelin.spotify.model_objects.specification.Paging;
import se.michaelthelin.spotify.model_objects.specification.TrackSimplified;
import se.michaelthelin.spotify.requests.data.albums.GetAlbumsTracksRequest;
import se.michaelthelin.spotify.requests.data.albums.GetSeveralAlbumsRequest;

@ExtendWith(MockitoExtension.class)
class SpotifyServiceGetAlbumsTest {
	@InjectMocks
	private SpotifyService spotifyService;

	@Mock
	private SpotifyApi spotifyApi;

	@Mock
	private GetSeveralAlbumsRequest.Builder getSeveralAlbumsRequestBuilderMock;

	@Mock
	private GetSeveralAlbumsRequest getSeveralAlbumsRequestMock;

	@Mock
	private GetAlbumsTracksRequest.Builder getAlbumsTracksRequestBuilderMock;

	@Mock
	private GetAlbumsTracksRequest getAlbumsTracksRequestMock;

	@Test
	void testGetAlbumsEmptyIdList () {
		final List<AlbumEntity> albums = spotifyService.getAlbums(new ArrayList<>());

		verifyNoInteractions(spotifyApi,
			getSeveralAlbumsRequestBuilderMock,
			getSeveralAlbumsRequestMock,
			getAlbumsTracksRequestBuilderMock,
			getAlbumsTracksRequestMock);
		assertTrue(albums.isEmpty());
	}

	@Test
	void testGetAlbumsNoneFound () throws ParseException, SpotifyWebApiException, IOException {
		when(spotifyApi.getSeveralAlbums("albumId")).thenReturn(getSeveralAlbumsRequestBuilderMock);
		when(getSeveralAlbumsRequestBuilderMock.build()).thenReturn(getSeveralAlbumsRequestMock);

		when(getSeveralAlbumsRequestMock.execute()).thenReturn(new Album[0]);

		final List<AlbumEntity> albumEntities = spotifyService.getAlbums(Arrays.asList("albumId"));

		verifyNoInteractions(getAlbumsTracksRequestBuilderMock,
			getAlbumsTracksRequestMock);
		assertTrue(albumEntities.isEmpty());
	}

	@Test
	void testGetAlbumsSinglePartition () throws ParseException, SpotifyWebApiException, IOException {
		when(spotifyApi.getSeveralAlbums("albumId1", "albumId2")).thenReturn(getSeveralAlbumsRequestBuilderMock);
		when(getSeveralAlbumsRequestBuilderMock.build()).thenReturn(getSeveralAlbumsRequestMock);

		final Album[] albumResponse = {
			new Album.Builder().setId("albumId1").build(),
			new Album.Builder().setId("albumId2").build()
		};
		when(getSeveralAlbumsRequestMock.execute()).thenReturn(albumResponse);

		final List<AlbumEntity> albumEntities = spotifyService.getAlbums(Arrays.asList("albumId1", "albumId2"));

		verifyNoInteractions(getAlbumsTracksRequestBuilderMock,
			getAlbumsTracksRequestMock);
		assertTrue(albumEntities.contains(AlbumEntity.builder().id("albumId1").build()));
		assertTrue(albumEntities.contains(AlbumEntity.builder().id("albumId2").build()));
	}

	@Test
	void testGetAlbumsMultiplePartitions () throws ParseException, SpotifyWebApiException, IOException {
		final GetSeveralAlbumsRequest.Builder getSeveralAlbumsRequestBuilderMock2 =
			mock(GetSeveralAlbumsRequest.Builder.class);
		final GetSeveralAlbumsRequest getSeveralAlbumsRequestMock2 = mock(GetSeveralAlbumsRequest.class);

		setupTestGetAlbumsMultiplePartitions(getSeveralAlbumsRequestBuilderMock2, getSeveralAlbumsRequestMock2);

		final List<String> albumIds = IntStream.rangeClosed(1, SpotifyService.maxAlbumIdsPerRequest * 2)
			.mapToObj(index -> "albumId" + index)
			.toList();
		final List<AlbumEntity> albumEntities = spotifyService.getAlbums(albumIds);

		verifyNoInteractions(getAlbumsTracksRequestBuilderMock,
			getAlbumsTracksRequestMock);
		IntStream.rangeClosed(1, SpotifyService.maxAlbumIdsPerRequest * 2)
			.forEach(index -> assertTrue(albumEntities
				.contains(AlbumEntity.builder().id("albumId" + index).build())));
	}

	private void setupTestGetAlbumsMultiplePartitions (
		final GetSeveralAlbumsRequest.Builder getSeveralAlbumsRequestBuilderMock2,
		final GetSeveralAlbumsRequest getSeveralAlbumsRequestMock2) throws IOException,
		SpotifyWebApiException,
		ParseException {
		final List<String> albumIdsFirstRequest = IntStream.rangeClosed(1, SpotifyService.maxAlbumIdsPerRequest)
			.mapToObj(index -> "albumId" + index)
			.toList();
		when(spotifyApi.getSeveralAlbums(albumIdsFirstRequest.toArray(new String[0])))
			.thenReturn(getSeveralAlbumsRequestBuilderMock);
		when(getSeveralAlbumsRequestBuilderMock.build()).thenReturn(getSeveralAlbumsRequestMock);
		final List<Album> firstRequestResponse = IntStream.rangeClosed(1, SpotifyService.maxAlbumIdsPerRequest)
			.mapToObj(index -> new Album.Builder().setId("albumId" + index).build())
			.toList();
		when(getSeveralAlbumsRequestMock.execute()).thenReturn(firstRequestResponse.toArray(new Album[0]));

		final List<String> albumIdsSecondRequest =
			IntStream.rangeClosed(SpotifyService.maxAlbumIdsPerRequest + 1, SpotifyService.maxAlbumIdsPerRequest * 2)
				.mapToObj(index -> "albumId" + index)
				.toList();
		when(spotifyApi.getSeveralAlbums(albumIdsSecondRequest.toArray(new String[0])))
			.thenReturn(getSeveralAlbumsRequestBuilderMock2);
		when(getSeveralAlbumsRequestBuilderMock2.build()).thenReturn(getSeveralAlbumsRequestMock2);
		final List<Album> secondRequestResponse =
			IntStream.rangeClosed(SpotifyService.maxAlbumIdsPerRequest + 1, SpotifyService.maxAlbumIdsPerRequest * 2)
				.mapToObj(index -> new Album.Builder().setId("albumId" + index).build())
				.toList();
		when(getSeveralAlbumsRequestMock2.execute()).thenReturn(secondRequestResponse.toArray(new Album[0]));
	}

	@Test
	void testGetAlbumWithGetAlbumOtherTracksTrackRequest () throws ParseException, SpotifyWebApiException, IOException {
		setupTestGetAlbumWithGetAlbumOtherTracksTrackRequest();

		final List<AlbumEntity> albumEntities = spotifyService.getAlbums(Arrays.asList("albumId"));

		verify(getAlbumsTracksRequestBuilderMock).offset(SpotifyService.maxItemsPerPage);
		verify(getAlbumsTracksRequestBuilderMock).limit(SpotifyService.maxItemsPerPage);

		final Set<TrackEntity> trackEntities = IntStream.rangeClosed(1, SpotifyService.maxItemsPerPage + 1)
			.mapToObj(index -> TrackEntity.builder().id("trackId" + index).build())
			.collect(Collectors.toSet());
		assertTrue(albumEntities
			.contains(
				AlbumEntity.builder()
					.id("albumId")
					.trackCount(SpotifyService.maxItemsPerPage + 1)
					.tracks(trackEntities)
					.build()));
	}

	private void setupTestGetAlbumWithGetAlbumOtherTracksTrackRequest () throws IOException,
		SpotifyWebApiException,
		ParseException {
		when(spotifyApi.getSeveralAlbums("albumId")).thenReturn(getSeveralAlbumsRequestBuilderMock);
		when(getSeveralAlbumsRequestBuilderMock.build()).thenReturn(getSeveralAlbumsRequestMock);

		final List<TrackSimplified> trackSimplifiedPage1 = IntStream.rangeClosed(1, SpotifyService.maxItemsPerPage)
			.mapToObj(index -> new TrackSimplified.Builder().setId("trackId" + index).build())
			.toList();
		final Album[] albumResponse = {
			new Album.Builder().setId("albumId")
				.setTracks(new Paging.Builder<TrackSimplified>().setItems(
					trackSimplifiedPage1.toArray(new TrackSimplified[0]))
					.setTotal(SpotifyService.maxItemsPerPage + 1)
					.build())
				.build()
		};
		when(getSeveralAlbumsRequestMock.execute()).thenReturn(albumResponse);

		when(spotifyApi.getAlbumsTracks("albumId")).thenReturn(getAlbumsTracksRequestBuilderMock);
		when(getAlbumsTracksRequestBuilderMock.offset(SpotifyService.maxItemsPerPage))
			.thenReturn(getAlbumsTracksRequestBuilderMock);
		when(getAlbumsTracksRequestBuilderMock.limit(SpotifyService.maxItemsPerPage))
			.thenReturn(getAlbumsTracksRequestBuilderMock);
		when(getAlbumsTracksRequestBuilderMock.build()).thenReturn(getAlbumsTracksRequestMock);

		final TrackSimplified[] trackSimplifiedPage2 =
			{ new TrackSimplified.Builder().setId("trackId" + (SpotifyService.maxItemsPerPage + 1)).build() };
		when(getAlbumsTracksRequestMock.execute()).thenReturn(new Paging.Builder<TrackSimplified>()
			.setItems(trackSimplifiedPage2)
			.build());
	}

	@Test
	void testGetAlbumWithMultipleGetAlbumOtherTracksTrackRequests () throws ParseException,
		SpotifyWebApiException,
		IOException {
		final GetAlbumsTracksRequest.Builder getAlbumsTracksRequestBuilderMock2 =
			mock(GetAlbumsTracksRequest.Builder.class);
		final GetAlbumsTracksRequest getAlbumsTracksRequest2 = mock(GetAlbumsTracksRequest.class);

		setupTestGetAlbumWithMultipleGetAlbumOtherTracksTrackRequests(getAlbumsTracksRequestBuilderMock2,
			getAlbumsTracksRequest2);

		final List<AlbumEntity> albumEntities = spotifyService.getAlbums(Arrays.asList("albumId"));

		verify(getAlbumsTracksRequestBuilderMock).offset(SpotifyService.maxItemsPerPage);
		verify(getAlbumsTracksRequestBuilderMock).limit(SpotifyService.maxItemsPerPage);

		verify(getAlbumsTracksRequestBuilderMock).offset(SpotifyService.maxItemsPerPage * 2);
		verify(getAlbumsTracksRequestBuilderMock2).limit(SpotifyService.maxItemsPerPage);

		final Set<TrackEntity> trackEntities = IntStream.rangeClosed(1, SpotifyService.maxItemsPerPage * 3)
			.mapToObj(index -> TrackEntity.builder().id("trackId" + index).build())
			.collect(Collectors.toSet());
		assertTrue(albumEntities
			.contains(
				AlbumEntity.builder()
					.id("albumId")
					.trackCount(SpotifyService.maxItemsPerPage * 3)
					.tracks(trackEntities)
					.build()));
	}

	private void setupTestGetAlbumWithMultipleGetAlbumOtherTracksTrackRequests (
		final GetAlbumsTracksRequest.Builder getAlbumsTracksRequestBuilderMock2,
		final GetAlbumsTracksRequest getAlbumsTracksRequest2) throws IOException,
		SpotifyWebApiException,
		ParseException {
		when(spotifyApi.getSeveralAlbums("albumId")).thenReturn(getSeveralAlbumsRequestBuilderMock);
		when(getSeveralAlbumsRequestBuilderMock.build()).thenReturn(getSeveralAlbumsRequestMock);

		final List<TrackSimplified> trackSimplifiedPage1 = IntStream.rangeClosed(1, SpotifyService.maxItemsPerPage)
			.mapToObj(index -> new TrackSimplified.Builder().setId("trackId" + index).build())
			.toList();
		final Album[] albumResponse = {
			new Album.Builder().setId("albumId")
				.setTracks(new Paging.Builder<TrackSimplified>().setItems(
					trackSimplifiedPage1.toArray(new TrackSimplified[0]))
					.setTotal(SpotifyService.maxItemsPerPage * 3)
					.build())
				.build()
		};
		when(getSeveralAlbumsRequestMock.execute()).thenReturn(albumResponse);

		when(spotifyApi.getAlbumsTracks("albumId")).thenReturn(getAlbumsTracksRequestBuilderMock);
		setupTrackPage2();
		setupTrackPage3(getAlbumsTracksRequestBuilderMock2, getAlbumsTracksRequest2);
	}

	private void setupTrackPage3 (final GetAlbumsTracksRequest.Builder getAlbumsTracksRequestBuilderMock2,
		final GetAlbumsTracksRequest getAlbumsTracksRequest2) throws IOException,
		SpotifyWebApiException,
		ParseException {
		when(getAlbumsTracksRequestBuilderMock.offset(SpotifyService.maxItemsPerPage * 2))
			.thenReturn(getAlbumsTracksRequestBuilderMock2);
		when(getAlbumsTracksRequestBuilderMock2.limit(SpotifyService.maxItemsPerPage))
			.thenReturn(getAlbumsTracksRequestBuilderMock2);
		when(getAlbumsTracksRequestBuilderMock2.build()).thenReturn(getAlbumsTracksRequest2);

		final List<TrackSimplified> trackSimplifiedPage3 =
			IntStream.rangeClosed(SpotifyService.maxItemsPerPage * 2 + 1, SpotifyService.maxItemsPerPage * 3)
				.mapToObj(index -> new TrackSimplified.Builder().setId("trackId" + index).build())
				.toList();
		when(getAlbumsTracksRequest2.execute()).thenReturn(new Paging.Builder<TrackSimplified>()
			.setItems(trackSimplifiedPage3.toArray(new TrackSimplified[0]))
			.build());
	}

	private void setupTrackPage2 () throws IOException, SpotifyWebApiException, ParseException {
		when(getAlbumsTracksRequestBuilderMock.offset(SpotifyService.maxItemsPerPage))
			.thenReturn(getAlbumsTracksRequestBuilderMock);
		when(getAlbumsTracksRequestBuilderMock.limit(SpotifyService.maxItemsPerPage))
			.thenReturn(getAlbumsTracksRequestBuilderMock);
		when(getAlbumsTracksRequestBuilderMock.build()).thenReturn(getAlbumsTracksRequestMock);

		final List<TrackSimplified> trackSimplifiedPage2 =
			IntStream.rangeClosed(SpotifyService.maxItemsPerPage + 1, SpotifyService.maxItemsPerPage * 2)
				.mapToObj(index -> new TrackSimplified.Builder().setId("trackId" + index).build())
				.toList();
		when(getAlbumsTracksRequestMock.execute()).thenReturn(new Paging.Builder<TrackSimplified>()
			.setItems(trackSimplifiedPage2.toArray(new TrackSimplified[0]))
			.setNext("nextUrl")
			.build());
	}
}
