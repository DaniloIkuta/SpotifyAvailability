package br.daniloikuta.spotifyavailability;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.actuate.autoconfigure.security.servlet.ManagementWebSecurityAutoConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.data.envers.repository.support.EnversRevisionRepositoryFactoryBean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories(repositoryFactoryBeanClass = EnversRevisionRepositoryFactoryBean.class)
@EnableAutoConfiguration(exclude = {
	SecurityAutoConfiguration.class,
	ManagementWebSecurityAutoConfiguration.class
})
public class SpotifyAvailabilityApplication {

	public static void main (final String[] args) {
		SpringApplication.run(SpotifyAvailabilityApplication.class, args);
	}

}
