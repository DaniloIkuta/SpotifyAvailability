package br.daniloikuta.spotifyavailability.configuration;

import java.io.IOException;

import org.apache.hc.core5.http.ParseException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import se.michaelthelin.spotify.SpotifyApi;
import se.michaelthelin.spotify.exceptions.SpotifyWebApiException;
import se.michaelthelin.spotify.model_objects.credentials.ClientCredentials;

@Configuration
@EnableScheduling
public class SpotifyClientConfiguration {

	@Value("${spotify.client-id}")
	private String clientId;

	@Value("${spotify.client-secret}")
	private String clientSecret;

	private SpotifyApi spotifyApi;

	@Bean(name = "spotifyApi")
	public SpotifyApi create () throws ParseException, SpotifyWebApiException, IOException {
		spotifyApi =
			new SpotifyApi.Builder().setClientId(clientId).setClientSecret(clientSecret).build();

		final ClientCredentials clientCredentials = spotifyApi.clientCredentials().build().execute();
		spotifyApi.setAccessToken(clientCredentials.getAccessToken());

		return spotifyApi;
	}

	@Scheduled(fixedDelayString = "${spotify.access-token.expiration}")
	private void reloadAccessToken () throws ParseException, SpotifyWebApiException, IOException {
		final ClientCredentials clientCredentials = spotifyApi.clientCredentials().build().execute();
		spotifyApi.setAccessToken(clientCredentials.getAccessToken());
	}
}
