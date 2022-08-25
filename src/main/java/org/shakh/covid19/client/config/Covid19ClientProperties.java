package org.shakh.covid19.client.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

import java.net.URI;
import java.time.Duration;

@ConfigurationProperties(prefix = "api.covid19.client")
@ConstructorBinding
@Data
public class Covid19ClientProperties {

    /**
     * URI to COVID-19 API.
     */
    private final URI uri;

    /**
     * Connect timeout to server.
     */
    private final Duration connectTimeout;

    /**
     * Read timeout to server.
     */
    private final Duration readTimeout;

}
