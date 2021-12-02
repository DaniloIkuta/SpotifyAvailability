package br.daniloikuta.spotifyavailability.configuration;

import java.util.Optional;

import org.hibernate.envers.RevisionListener;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;

import br.daniloikuta.spotifyavailability.entity.AuditRevisionEntity;

public class AuditRevisionListener implements RevisionListener {

	@Override
	public void newRevision (final Object revisionEntity) {
		final String currentUser = Optional.ofNullable(SecurityContextHolder.getContext())
			.map(SecurityContext::getAuthentication)
			.filter(Authentication::isAuthenticated)
			.map(Authentication::getPrincipal)
			.map(User.class::cast)
			.map(User::getUsername)
			.orElse("Unknown-User");

		final AuditRevisionEntity audit = (AuditRevisionEntity) revisionEntity;
		audit.setUser(currentUser);
	}

}
