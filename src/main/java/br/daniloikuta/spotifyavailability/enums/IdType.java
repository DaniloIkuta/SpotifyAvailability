package br.daniloikuta.spotifyavailability.enums;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum IdType {
	ARTIST("artist"),
	ALBUM("album"),
	TRACK("track");

	private final String type;

	private static final Map<String, IdType> map = new HashMap<>();

	static {
		for (final IdType idType : IdType.values()) {
			map.put(idType.type, idType);
		}
	}

	public static IdType keyOf (final String type) {
		return Optional.ofNullable(map.get(type))
			.orElseThrow( () -> new EnumConstantNotPresentException(IdType.class, type));
	}
}
