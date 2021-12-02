package br.daniloikuta.spotifyavailability.enums;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ReleaseDatePrecision {
	DAY("day"),
	MONTH("month"),
	YEAR("year");

	private final String precision;

	private static final Map<String, ReleaseDatePrecision> map = new HashMap<>();

	static {
		for (final ReleaseDatePrecision releaseDatePrecision : ReleaseDatePrecision.values()) {
			map.put(releaseDatePrecision.precision, releaseDatePrecision);
		}
	}

	public static ReleaseDatePrecision keyOf (final String precision) {
		return Optional.ofNullable(map.get(precision))
			.orElseThrow( () -> new EnumConstantNotPresentException(ReleaseDatePrecision.class, precision));
	}
}
