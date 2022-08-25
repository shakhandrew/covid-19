package org.shakh.covid19.client.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
@EnableConfigurationProperties(Covid19ClientProperties.class)
public class Covid19ClientConfiguration {

    @Bean
    public RestTemplate restTemplate(final Covid19ClientProperties properties) {
        return new RestTemplateBuilder()
                .setReadTimeout(properties.getReadTimeout())
                .setConnectTimeout(properties.getConnectTimeout())
                .build();
    }

}
