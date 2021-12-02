package br.daniloikuta.spotifyavailability.service;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;

import org.apache.hc.core5.http.ParseException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import br.daniloikuta.spotifyavailability.entity.TrackEntity;
import se.michaelthelin.spotify.SpotifyApi;
import se.michaelthelin.spotify.exceptions.SpotifyWebApiException;
import se.michaelthelin.spotify.model_objects.specification.Track;
import se.michaelthelin.spotify.requests.data.tracks.GetSeveralTracksRequest;
import se.michaelthelin.spotify.requests.data.tracks.GetSeveralTracksRequest.Builder;

@ExtendWith(MockitoExtension.class)
class SpotifyServiceGetTracksTest {

	@InjectMocks
	private SpotifyService spotifyService;

	@Mock
	private SpotifyApi spotifyApi;

	@Mock
	private GetSeveralTracksRequest.Builder getSeveralTracksRequestBuilderMock;

	@Mock
	private GetSeveralTracksRequest getSeveralTracksRequestMock;

	@Test
	void testGetTracksEmptyIdList () {
		final List<TrackEntity> tracks = spotifyService.getTracks(new ArrayList<>());

		verifyNoInteractions(spotifyApi, getSeveralTracksRequestBuilderMock, getSeveralTracksRequestMock);
		assertTrue(tracks.isEmpty());
	}

	@Test
	void testGetTracksNoneFound () throws ParseException, SpotifyWebApiException, IOException {
		when(spotifyApi.getSeveralTracks("trackId")).thenReturn(getSeveralTracksRequestBuilderMock);
		when(getSeveralTracksRequestBuilderMock.build()).thenReturn(getSeveralTracksRequestMock);
		when(getSeveralTracksRequestMock.execute()).thenReturn(new Track[0]);

		final List<TrackEntity> tracks = spotifyService.getTracks(Arrays.asList("trackId"));

		assertTrue(tracks.isEmpty());
	}

	@Test
	void testGetTracksSinglePartition () throws ParseException, SpotifyWebApiException, IOException {
		when(spotifyApi.getSeveralTracks("trackId1", "trackId2")).thenReturn(getSeveralTracksRequestBuilderMock);
		when(getSeveralTracksRequestBuilderMock.build()).thenReturn(getSeveralTracksRequestMock);

		final Track[] trackResponse = {
			new Track.Builder().setId("trackId1").build(),
			new Track.Builder().setId("trackId2").build()
		};
		when(getSeveralTracksRequestMock.execute()).thenReturn(trackResponse);

		final List<TrackEntity> trackEntities = spotifyService.getTracks(Arrays.asList("trackId1", "trackId2"));
		assertTrue(trackEntities.contains(TrackEntity.builder().id("trackId1").build()));
		assertTrue(trackEntities.contains(TrackEntity.builder().id("trackId2").build()));
	}

	@Test
	void testGetTracksMultiplePartitions () throws ParseException, SpotifyWebApiException, IOException {
		final GetSeveralTracksRequest.Builder getSeveralTracksRequestBuilderMock2 =
			mock(GetSeveralTracksRequest.Builder.class);
		final GetSeveralTracksRequest getSeveralTracksRequestMock2 = mock(GetSeveralTracksRequest.class);

		setupTestGetTracksMultiplePartitions(getSeveralTracksRequestBuilderMock2, getSeveralTracksRequestMock2);

		final List<String> trackIds = IntStream.rangeClosed(1, SpotifyService.maxTrackIdsPerRequest + 1)
			.mapToObj(index -> "trackId" + index)
			.toList();
		final List<TrackEntity> trackEntities = spotifyService.getTracks(trackIds);

		IntStream.rangeClosed(1, SpotifyService.maxTrackIdsPerRequest + 1)
			.forEach(index -> assertTrue(
				trackEntities.contains(TrackEntity.builder().id("trackId" + index).build())));
	}

	private void setupTestGetTracksMultiplePartitions (final Builder getSeveralTracksRequestBuilderMock2,
		final GetSeveralTracksRequest getSeveralTracksRequestMock2) throws IOException,
		SpotifyWebApiException,
		ParseException {
		final List<String> trackIdsFirstRequest = IntStream.rangeClosed(1, SpotifyService.maxTrackIdsPerRequest)
			.mapToObj(index -> "trackId" + index)
			.toList();
		when(spotifyApi.getSeveralTracks(trackIdsFirstRequest.toArray(new String[0])))
			.thenReturn(getSeveralTracksRequestBuilderMock);
		when(getSeveralTracksRequestBuilderMock.build()).thenReturn(getSeveralTracksRequestMock);

		final List<Track> firstRequestResponse = IntStream.rangeClosed(1, SpotifyService.maxTrackIdsPerRequest)
			.mapToObj(index -> new Track.Builder().setId("trackId" + index).build())
			.toList();
		when(getSeveralTracksRequestMock.execute()).thenReturn(firstRequestResponse.toArray(new Track[0]));

		when(spotifyApi.getSeveralTracks("trackId" + (SpotifyService.maxTrackIdsPerRequest + 1)))
			.thenReturn(getSeveralTracksRequestBuilderMock2);
		when(getSeveralTracksRequestBuilderMock2.build()).thenReturn(getSeveralTracksRequestMock2);

		final Track[] secondRequestResponse =
			{ new Track.Builder().setId("trackId" + (SpotifyService.maxTrackIdsPerRequest + 1)).build() };
		when(getSeveralTracksRequestMock2.execute())
			.thenReturn(secondRequestResponse);
	}
}
