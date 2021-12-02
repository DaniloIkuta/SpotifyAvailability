package br.daniloikuta.spotifyavailability;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.envers.repository.support.EnversRevisionRepositoryFactoryBean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories(repositoryFactoryBeanClass = EnversRevisionRepositoryFactoryBean.class)
public class SpotifyAvailabilityApplication {

	public static void main (final String[] args) {
		SpringApplication.run(SpotifyAvailabilityApplication.class, args);
	}

}
