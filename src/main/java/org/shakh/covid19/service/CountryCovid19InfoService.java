package org.shakh.covid19.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.shakh.covid19.client.Covid19Client;
import org.shakh.covid19.client.dto.cases.CasesResponse;
import org.shakh.covid19.client.dto.history.HistoryRequest;
import org.shakh.covid19.client.dto.history.HistoryResponse;
import org.shakh.covid19.client.dto.history.HistoryStatus;
import org.shakh.covid19.client.dto.vaccines.VaccinesResponse;
import org.shakh.covid19.service.dto.CountryCovid19InfoDto;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Service for working with information about covid19 for country.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class CountryCovid19InfoService {

    private final Covid19Client covid19Client;
    private static final BigDecimal PERCENT_100 = new BigDecimal("100");

    /**
     * Getting covid19 info for country.
     *
     * @param country country name
     * @return information about covid19 for the country.
     */
    public CountryCovid19InfoDto getCovid19Info(final String country) {
        log.debug("Starting to formation information for the country {} about covid19.", country);
        CasesResponse cases = covid19Client.getCases(country);
        VaccinesResponse vaccines = covid19Client.getVaccines(country);

        var historyRequest = new HistoryRequest(country, HistoryStatus.CONFIRMED);
        HistoryResponse history = covid19Client.getHistory(historyRequest);

        BigDecimal vaccinatedLevel = getVaccinatedLevel(vaccines.getPeopleVaccinated(), vaccines.getPopulation());
        Long newConfirmedCases = getNewConfirmedCases(history.dates(), cases.getConfirmed());

        var countryCovidInfo = CountryCovid19InfoDto.builder()
                .country(cases.getCountry())
                .confirmed(cases.getConfirmed())
                .recovered(cases.getRecovered())
                .death(cases.getDeaths())
                .vaccinatedLevel(vaccinatedLevel)
                .newConfirmedCases(newConfirmedCases)
                .build();

        log.debug("Ending to formation information for the country about covid19 {}.", countryCovidInfo);
        return countryCovidInfo;
    }

    /**
     * Getting vaccinated level.
     *
     * @param peopleVaccinated the number of vaccinated people.
     * @param population       the number of population.
     * @return vaccinated level
     */
    private BigDecimal getVaccinatedLevel(final Long peopleVaccinated, final Long population) {
        if (peopleVaccinated == null || population == null) {
            log.warn("Missing information about vaccinated people or number of population.");
            return BigDecimal.ZERO;
        }

        return BigDecimal.valueOf(peopleVaccinated)
                .divide(BigDecimal.valueOf(population), 2, RoundingMode.HALF_UP)
                .multiply(PERCENT_100);
    }

    /**
     * Getting new confirmed cases.
     *
     * @param historicalCases map date and cases.
     * @param confirmedCases  confirmed cases.
     * @return new confirmed cases
     */
    private Long getNewConfirmedCases(final Map<LocalDate, Long> historicalCases,
                                      final Long confirmedCases) {
        if (historicalCases == null) {
            log.warn("Missing historical information about covid19.");
            return null;
        }

        Optional<Map.Entry<LocalDate, Long>> historyInformationOpt =
                historicalCases.entrySet().stream()
                        .min(Map.Entry.comparingByKey(Comparator.reverseOrder()));

        var newConfirmedCases = new AtomicReference<Long>();
        historyInformationOpt.ifPresent(
                historyInfo -> newConfirmedCases.set(confirmedCases - historyInfo.getValue())
        );

        return newConfirmedCases.get();
    }

}
