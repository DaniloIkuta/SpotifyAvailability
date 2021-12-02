package br.daniloikuta.spotifyavailability.enums;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum AlbumType {
	ALBUM("album"),
	COMPILATION("compilation"),
	SINGLE("single");

	private final String type;

	private static final Map<String, AlbumType> map = new HashMap<>();

	static {
		for (final AlbumType albumType : AlbumType.values()) {
			map.put(albumType.type, albumType);
		}
	}

	public static AlbumType keyOf (final String type) {
		return Optional.ofNullable(map.get(type))
			.orElseThrow( () -> new EnumConstantNotPresentException(AlbumType.class, type));
	}
}
