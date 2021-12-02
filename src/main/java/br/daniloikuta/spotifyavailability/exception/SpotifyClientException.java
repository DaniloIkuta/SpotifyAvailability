package br.daniloikuta.spotifyavailability.exception;

public class SpotifyClientException extends RuntimeException {

	private static final long serialVersionUID = -294342033973838009L;

	public SpotifyClientException () {
		super();
	}

	public SpotifyClientException (final String message,
		final Throwable cause,
		final boolean enableSuppression,
		final boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public SpotifyClientException (final String message, final Throwable cause) {
		super(message, cause);
	}

	public SpotifyClientException (final String message) {
		super(message);
	}

	public SpotifyClientException (final Throwable cause) {
		super(cause);
	}

}
