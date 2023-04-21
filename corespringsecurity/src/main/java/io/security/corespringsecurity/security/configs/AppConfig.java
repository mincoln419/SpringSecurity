package io.security.corespringsecurity.security.configs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.security.corespringsecurity.repository.ResourcesRepository;
import io.security.corespringsecurity.service.SecurityResourceService;

@Configuration
public class AppConfig {

	@Bean
	public SecurityResourceService securityResourceService(ResourcesRepository repository) {
		SecurityResourceService securityResourceService = new SecurityResourceService(repository);
		return securityResourceService;
	}


}
