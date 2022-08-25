package org.shakh.covid19.service;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.shakh.covid19.client.Covid19Client;
import org.shakh.covid19.client.dto.cases.CasesResponse;
import org.shakh.covid19.client.dto.history.HistoryRequest;
import org.shakh.covid19.client.dto.history.HistoryResponse;
import org.shakh.covid19.client.dto.history.HistoryStatus;
import org.shakh.covid19.client.dto.vaccines.VaccinesResponse;
import org.shakh.covid19.service.dto.CountryCovid19InfoDto;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

class CountryCovid19InfoServiceTest {

    private CountryCovid19InfoService countryCovid19InfoService;
    private Covid19Client covid19Client;

    @BeforeEach
    void init() {
        covid19Client = Mockito.mock(Covid19Client.class);
        countryCovid19InfoService = new CountryCovid19InfoService(covid19Client);
    }

    @Test
    void getCovid19Info() {
        String country = RandomStringUtils.randomAlphanumeric(5);

        CasesResponse casesResponse = getCasesResponse(country);
        Mockito.when(covid19Client.getCases(country)).thenReturn(casesResponse);

        VaccinesResponse vaccinesResponse = getVaccinesResponse();
        Mockito.when(covid19Client.getVaccines(country)).thenReturn(vaccinesResponse);

        HistoryResponse historyResponse = getHistoryResponse();
        Mockito.when(covid19Client.getHistory(Mockito.eq(new HistoryRequest(country, HistoryStatus.CONFIRMED))))
                .thenReturn(historyResponse);

        BigDecimal vaccinatedLevel = BigDecimal.valueOf(vaccinesResponse.getPeopleVaccinated())
                .divide(BigDecimal.valueOf(vaccinesResponse.getPopulation()), 2, RoundingMode.HALF_UP)
                .multiply(new BigDecimal(100));
        Long history = historyResponse.dates().entrySet().stream()
                .min(Map.Entry.comparingByKey(Comparator.reverseOrder()))
                .get()
                .getValue();

        long newConfirmedCases = casesResponse.getConfirmed() - history;

        CountryCovid19InfoDto result = countryCovid19InfoService.getCovid19Info(country);

        assertNotNull(result);
        assertEquals(country, result.getCountry());
        assertEquals(casesResponse.getConfirmed(), result.getConfirmed());
        assertEquals(casesResponse.getRecovered(), result.getRecovered());
        assertEquals(casesResponse.getDeaths(), result.getDeath());
        assertEquals(vaccinatedLevel, result.getVaccinatedLevel());
        assertEquals(newConfirmedCases, result.getNewConfirmedCases());
    }

    @Test
    void getCovid19InfoEmptyInfo() {
        String country = RandomStringUtils.randomAlphanumeric(5);

        CasesResponse casesResponse = getCasesResponse(country);
        Mockito.when(covid19Client.getCases(country)).thenReturn(casesResponse);

        VaccinesResponse vaccinesResponse = VaccinesResponse.builder().build();
        Mockito.when(covid19Client.getVaccines(country)).thenReturn(vaccinesResponse);

        HistoryResponse historyResponse = new HistoryResponse(null);
        Mockito.when(covid19Client.getHistory(Mockito.eq(new HistoryRequest(country, HistoryStatus.CONFIRMED))))
                .thenReturn(historyResponse);

        CountryCovid19InfoDto result = countryCovid19InfoService.getCovid19Info(country);

        assertNotNull(result);
        assertEquals(country, result.getCountry());
        assertEquals(casesResponse.getConfirmed(), result.getConfirmed());
        assertEquals(casesResponse.getRecovered(), result.getRecovered());
        assertEquals(casesResponse.getDeaths(), result.getDeath());
        assertEquals(BigDecimal.ZERO, result.getVaccinatedLevel());
        assertNull(result.getNewConfirmedCases());
    }

    private CasesResponse getCasesResponse(final String country) {
        return CasesResponse.builder()
                .confirmed(Long.valueOf(RandomStringUtils.randomNumeric(4)))
                .recovered(Long.valueOf(RandomStringUtils.randomNumeric(4)))
                .deaths(Long.valueOf(RandomStringUtils.randomNumeric(4)))
                .country(country)
                .build();
    }

    private VaccinesResponse getVaccinesResponse() {
        return VaccinesResponse.builder()
                .peopleVaccinated(Long.valueOf(RandomStringUtils.randomNumeric(4)))
                .population(Long.valueOf(RandomStringUtils.randomNumeric(4)))
                .build();
    }

    private HistoryResponse getHistoryResponse() {
        return new HistoryResponse(
                Map.of(LocalDate.of(2022, 1, 1), 100L)
        );
    }


}
