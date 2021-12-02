package br.daniloikuta.spotifyavailability.service;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.List;
import java.util.stream.IntStream;

import org.apache.hc.core5.http.ParseException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import br.daniloikuta.spotifyavailability.entity.AlbumEntity;
import se.michaelthelin.spotify.SpotifyApi;
import se.michaelthelin.spotify.exceptions.SpotifyWebApiException;
import se.michaelthelin.spotify.model_objects.specification.AlbumSimplified;
import se.michaelthelin.spotify.model_objects.specification.Paging;
import se.michaelthelin.spotify.requests.data.artists.GetArtistsAlbumsRequest;

@ExtendWith(MockitoExtension.class)
class SpotifyServiceGetArtistAlbumsTest {

	@InjectMocks
	private SpotifyService spotifyService;

	@Mock
	private SpotifyApi spotifyApi;

	@Mock
	private GetArtistsAlbumsRequest.Builder builderMock;

	@Mock
	private GetArtistsAlbumsRequest getArtistsAlbumsRequestMock;

	@Test
	void testGetArtistAlbumsNoAlbums () throws ParseException, SpotifyWebApiException, IOException {
		when(spotifyApi.getArtistsAlbums("artistId")).thenReturn(builderMock);
		when(builderMock.limit(SpotifyService.maxItemsPerPage)).thenReturn(builderMock);
		when(builderMock.build()).thenReturn(getArtistsAlbumsRequestMock);
		when(getArtistsAlbumsRequestMock.execute())
			.thenReturn(new Paging.Builder<AlbumSimplified>().build());

		final List<AlbumEntity> artistAlbums = spotifyService.getArtistAlbums("artistId");

		verify(builderMock).limit(SpotifyService.maxItemsPerPage);
		assertTrue(artistAlbums.isEmpty());
	}

	@Test
	void testGetArtistAlbumsSinglePage () throws ParseException, SpotifyWebApiException, IOException {
		when(spotifyApi.getArtistsAlbums("artistId")).thenReturn(builderMock);
		when(builderMock.limit(SpotifyService.maxItemsPerPage)).thenReturn(builderMock);
		when(builderMock.build()).thenReturn(getArtistsAlbumsRequestMock);

		final AlbumSimplified[] albums =
			{ new AlbumSimplified.Builder().setName("album1").build(),
				new AlbumSimplified.Builder().setName("album2").build() };
		when(getArtistsAlbumsRequestMock.execute())
			.thenReturn(new Paging.Builder<AlbumSimplified>().setItems(albums).build());

		final List<AlbumEntity> artistAlbums = spotifyService.getArtistAlbums("artistId");

		verify(builderMock).limit(SpotifyService.maxItemsPerPage);

		assertTrue(artistAlbums.contains(AlbumEntity.builder().name("album1").build()));
		assertTrue(artistAlbums.contains(AlbumEntity.builder().name("album2").build()));
	}

	@Test
	void testGetArtistAlbumsMultiplePages () throws ParseException, SpotifyWebApiException, IOException {
		final GetArtistsAlbumsRequest.Builder builderMock2 = mock(GetArtistsAlbumsRequest.Builder.class);
		final GetArtistsAlbumsRequest getArtistsAlbumsRequestMock2 = mock(GetArtistsAlbumsRequest.class);

		setupTestGetArtistAlbumsMultiplePages(builderMock2, getArtistsAlbumsRequestMock2);

		final List<AlbumEntity> artistAlbums = spotifyService.getArtistAlbums("artistId");

		verify(builderMock).limit(SpotifyService.maxItemsPerPage);
		verify(builderMock).offset(SpotifyService.maxItemsPerPage);
		verify(builderMock2).limit(SpotifyService.maxItemsPerPage);

		IntStream.rangeClosed(1, SpotifyService.maxItemsPerPage * 2)
			.forEach(index -> assertTrue(artistAlbums.contains(AlbumEntity.builder().name("album" + index).build())));
	}

	private void setupTestGetArtistAlbumsMultiplePages (final GetArtistsAlbumsRequest.Builder builderMock2,
		final GetArtistsAlbumsRequest getArtistsAlbumsRequestMock2) throws IOException,
		SpotifyWebApiException,
		ParseException {
		when(spotifyApi.getArtistsAlbums("artistId")).thenReturn(builderMock);
		when(builderMock.limit(SpotifyService.maxItemsPerPage)).thenReturn(builderMock);
		when(builderMock.build()).thenReturn(getArtistsAlbumsRequestMock);

		final List<AlbumSimplified> albumsPage1 = IntStream.rangeClosed(1, SpotifyService.maxItemsPerPage)
			.mapToObj(index -> new AlbumSimplified.Builder().setName("album" + index).build())
			.toList();
		when(getArtistsAlbumsRequestMock.execute())
			.thenReturn(
				new Paging.Builder<AlbumSimplified>().setItems(albumsPage1.toArray(new AlbumSimplified[0]))
					.setNext("nextUrl")
					.build());

		when(builderMock.offset(SpotifyService.maxItemsPerPage)).thenReturn(builderMock2);
		when(builderMock2.limit(SpotifyService.maxItemsPerPage)).thenReturn(builderMock2);
		when(builderMock2.build()).thenReturn(getArtistsAlbumsRequestMock2);

		final List<AlbumSimplified> albumsPage2 =
			IntStream.rangeClosed(SpotifyService.maxItemsPerPage + 1, SpotifyService.maxItemsPerPage * 2)
				.mapToObj(index -> new AlbumSimplified.Builder().setName("album" + index).build())
				.toList();
		when(getArtistsAlbumsRequestMock2.execute())
			.thenReturn(
				new Paging.Builder<AlbumSimplified>().setItems(albumsPage2.toArray(new AlbumSimplified[0])).build());
	}

}
