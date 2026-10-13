package com.mounika.student_registration.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

/**
 * Dedicated JPA configuration class.
 * Keeping @EnableJpaAuditing in a separate @Configuration class instead of on the main
 * @SpringBootApplication class ensures that web slice tests (@WebMvcTest) can run
 * without requiring a full JPA EntityManager or jpaMappingContext.
 */
@Configuration
@EnableJpaAuditing
public class JpaConfig {
}
