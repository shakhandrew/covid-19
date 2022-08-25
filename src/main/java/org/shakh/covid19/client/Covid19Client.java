package org.shakh.covid19.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.shakh.covid19.client.config.Covid19ClientProperties;
import org.shakh.covid19.client.dto.cases.CasesResponse;
import org.shakh.covid19.client.dto.history.HistoryRequest;
import org.shakh.covid19.client.dto.history.HistoryResponse;
import org.shakh.covid19.client.dto.vaccines.VaccinesResponse;
import org.shakh.covid19.client.error.Covid19ClientException;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

/**
 * Client for COVID-19 API.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class Covid19Client {

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;
    private final Covid19ClientProperties properties;

    /**
     * Get the information about cases of covid19.
     *
     * @param countryName country name
     * @return information about cases of covid19
     */
    public CasesResponse getCases(final String countryName) {
        URI requestUri = UriComponentsBuilder.fromUri(properties.getUri())
                .pathSegment("cases")
                .queryParam("country", countryName)
                .build()
                .toUri();

        log.debug("Sending the request for get information about cases of covid19 url:{} ", requestUri);
        try {
            ResponseEntity<String> response = restTemplate
                    .exchange(requestUri, HttpMethod.GET, null, String.class);
            return objectMapper.readValue(response.getBody(), CasesResponse.class);
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            throw new Covid19ClientException(
                    "Error while executing request for get information about cases of covid19. "
                            + e.getResponseBodyAsString());
        } catch (JsonProcessingException e) {
            throw new Covid19ClientException(
                    "Failed to deserialize message for get information about cases of covid19. "
                            + e.getMessage());
        }
    }

    /**
     * Get the information about vaccines of covid19.
     *
     * @param countryName country name
     * @return information about vaccines of covid19
     */
    public VaccinesResponse getVaccines(final String countryName) {
        URI requestUri = UriComponentsBuilder.fromUri(properties.getUri())
                .pathSegment("vaccines")
                .queryParam("country", countryName)
                .build()
                .toUri();

        log.debug("Sending the request for get information about vaccines of covid19 url:{} ", requestUri);
        try {
            ResponseEntity<String> response = restTemplate
                    .exchange(requestUri, HttpMethod.GET, null, String.class);
            return objectMapper.readValue(response.getBody(), VaccinesResponse.class);
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            throw new Covid19ClientException(
                    "Error while executing request for get information about vaccines of covid19. "
                            + e.getResponseBodyAsString());
        } catch (JsonProcessingException e) {
            throw new Covid19ClientException(
                    "Failed to deserialize message for get information about vaccines of covid19. "
                            + e.getMessage());
        }
    }

    /**
     * Get historical information on covid19 cases.
     *
     * @param request history request
     * @return historical information on covid19 cases
     */
    public HistoryResponse getHistory(final HistoryRequest request) {
        var requestUri = UriComponentsBuilder.fromUri(properties.getUri())
                .pathSegment("history")
                .queryParam("country", request.country())
                .queryParam("status", request.status().getName())
                .build()
                .toUri();

        log.debug("Sending the request for get historical information on covid19 cases url:{} ", requestUri);
        try {
            ResponseEntity<String> response = restTemplate
                    .exchange(requestUri, HttpMethod.GET, null, String.class);
            return objectMapper.readValue(response.getBody(), HistoryResponse.class);
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            throw new Covid19ClientException(
                    "Error while executing request for get historical information on covid19 cases. "
                            + e.getResponseBodyAsString());
        } catch (JsonProcessingException e) {
            throw new Covid19ClientException(
                    "Failed to deserialize message for get historical information on covid19 cases. "
                            + e.getMessage());
        }
    }

}
