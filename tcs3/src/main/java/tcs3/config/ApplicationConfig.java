package tcs3.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import tcs3.service.EntityService;
import tcs3.service.EntityServiceJPA;

@Configuration
public class ApplicationConfig {

	@Bean
	public EntityService entityService() {
		return new EntityServiceJPA();
	}

}
