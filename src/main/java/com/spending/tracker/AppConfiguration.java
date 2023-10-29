package com.spending.tracker;

import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Page;

import com.spending.tracker.jackson.PageDeserializer;

@Configuration
public class AppConfiguration {

	@Bean
	public Jackson2ObjectMapperBuilderCustomizer addCustomDeserializers() {
		return builder -> builder.deserializerByType(Page.class, new PageDeserializer());
	}
}
