package org.shakh.covid19.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.shakh.covid19.client.config.Covid19ClientProperties;
import org.shakh.covid19.client.dto.cases.CasesResponse;
import org.shakh.covid19.client.dto.history.HistoryRequest;
import org.shakh.covid19.client.dto.history.HistoryResponse;
import org.shakh.covid19.client.dto.history.HistoryStatus;
import org.shakh.covid19.client.dto.vaccines.VaccinesResponse;
import org.shakh.covid19.client.error.Covid19ClientException;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URISyntaxException;
import java.time.Duration;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class Covid19ClientTest {

    private Covid19Client covid19Client;
    private RestTemplate restTemplate;
    private Covid19ClientProperties properties;

    @BeforeEach
    void init() throws URISyntaxException {
        restTemplate = Mockito.mock(RestTemplate.class);
        ObjectMapper objectMapper = new ObjectMapper();
        properties = new Covid19ClientProperties(
                new URI("https://test/v1"),
                Duration.ZERO,
                Duration.ZERO
        );
        covid19Client = new Covid19Client(
                restTemplate,
                objectMapper,
                properties
        );
    }

    @Test
    void getCases() {
        Long confirmed = Long.valueOf(RandomStringUtils.randomNumeric(5));
        Long recovered = Long.valueOf(RandomStringUtils.randomNumeric(5));
        Long deaths = Long.valueOf(RandomStringUtils.randomNumeric(5));
        String country = RandomStringUtils.randomAlphanumeric(5);

        Mockito.when(restTemplate.exchange(Mockito.any(URI.class),
                        Mockito.eq(HttpMethod.GET),
                        Mockito.any(), Mockito.eq(String.class)))
                .thenReturn(
                        ResponseEntity.of(
                                Optional.of(getCase(confirmed, recovered, deaths, country))
                        )
                );

        CasesResponse res = covid19Client.getCases(country);

        assertNotNull(res);
        assertEquals(confirmed, res.getConfirmed());
        assertEquals(recovered, res.getRecovered());
        assertEquals(deaths, res.getDeaths());
        assertEquals(country, res.getCountry());
    }

    @Test
    void getCasesWithHttpClientErrorException() {
        String country = RandomStringUtils.randomAlphanumeric(5);

        Mockito.doThrow(new HttpClientErrorException(HttpStatus.NO_CONTENT))
                .when(restTemplate)
                .exchange(Mockito.any(URI.class),
                        Mockito.eq(HttpMethod.GET),
                        Mockito.any(),
                        Mockito.eq(String.class)
                );
        Assertions.assertThrows(Covid19ClientException.class,
                () -> covid19Client.getCases(country)
        );
    }

    @Test
    void getVaccine() {
        Long population = Long.valueOf(RandomStringUtils.randomNumeric(5));
        Long peopleVaccinated = Long.valueOf(RandomStringUtils.randomNumeric(5));
        String country = RandomStringUtils.randomAlphanumeric(5);

        Mockito.when(restTemplate.exchange(Mockito.any(URI.class),
                        Mockito.eq(HttpMethod.GET),
                        Mockito.any(), Mockito.eq(String.class)))
                .thenReturn(
                        ResponseEntity.of(
                                Optional.of(getVaccine(population, peopleVaccinated))
                        )
                );

        VaccinesResponse res = covid19Client.getVaccines(country);

        assertNotNull(res);
        assertEquals(population, res.getPopulation());
        assertEquals(peopleVaccinated, res.getPeopleVaccinated());
    }

    @Test
    void getVaccineWithHttpClientErrorException() {
        String country = RandomStringUtils.randomAlphanumeric(5);

        Mockito.doThrow(new HttpClientErrorException(HttpStatus.NO_CONTENT))
                .when(restTemplate)
                .exchange(Mockito.any(URI.class),
                        Mockito.eq(HttpMethod.GET),
                        Mockito.any(),
                        Mockito.eq(String.class)
                );
        Assertions.assertThrows(Covid19ClientException.class,
                () -> covid19Client.getVaccines(country)
        );
    }

    @Test
    void getHistory() throws JsonProcessingException {
        ObjectMapper objectMapper = Mockito.mock(ObjectMapper.class);
        covid19Client = new Covid19Client(
                restTemplate,
                objectMapper,
                properties
        );

        LocalDate date = LocalDate.of(2022, 1, 1);
        Map<LocalDate, Long> dates = Map.of(
                date, Long.valueOf(RandomStringUtils.randomNumeric(7)),
                LocalDate.of(2022, 1, 2), Long.valueOf(RandomStringUtils.randomNumeric(7)),
                LocalDate.of(2022, 1, 3), Long.valueOf(RandomStringUtils.randomNumeric(7))
        );
        String country = RandomStringUtils.randomAlphanumeric(5);

        String datesRs = getDates(dates);
        Mockito.when(restTemplate.exchange(Mockito.any(URI.class),
                        Mockito.eq(HttpMethod.GET),
                        Mockito.any(), Mockito.eq(String.class)))
                .thenReturn(ResponseEntity.of(Optional.of(datesRs)));
        Mockito.when(objectMapper.readValue(datesRs, HistoryResponse.class))
                .thenReturn(new HistoryResponse(dates));

        var req = new HistoryRequest(country, HistoryStatus.CONFIRMED);
        HistoryResponse res = covid19Client.getHistory(req);

        assertNotNull(res);
        assertEquals(dates.size(), res.dates().size());
        assertTrue(res.dates().containsKey(date));
    }

    @Test
    void getHistoryWithHttpClientErrorException() {
        String country = RandomStringUtils.randomAlphanumeric(5);
        var req = new HistoryRequest(country, HistoryStatus.CONFIRMED);

        Mockito.doThrow(new HttpClientErrorException(HttpStatus.NO_CONTENT))
                .when(restTemplate)
                .exchange(Mockito.any(URI.class),
                        Mockito.eq(HttpMethod.GET),
                        Mockito.any(),
                        Mockito.eq(String.class)
                );
        Assertions.assertThrows(Covid19ClientException.class,
                () -> covid19Client.getHistory(req)
        );
    }

    private String getCase(final Long confirmed, final Long recovered, final Long deaths, final String country) {
        return String.format("""
                {
                  "All": {
                    "confirmed": %s,
                    "recovered": %s,
                    "deaths": %s,
                    "country": "%s"
                  }
                }
                """, confirmed, recovered, deaths, country);
    }

    private String getVaccine(final Long population, final Long peopleVaccinated) {
        return String.format("""
                {
                  "All": {
                    "population": %s,
                    "people_vaccinated": %s
                  }
                }
                """, population, peopleVaccinated);
    }

    private String getDates(final Map<LocalDate, Long> dates) {

        List<String> datesList = dates.entrySet().stream()
                .map(f -> String.format("\"%s\": %s", f.getKey(), f.getValue()))
                .toList();
        return String.format("""
                {
                  "All": {
                       "dates": {
                         %s
                       }
                     }
                }
                """, String.join(",", datesList));
    }

}
