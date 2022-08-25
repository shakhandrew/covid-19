package org.shakh.covid19.service.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

/**
 * Information about covid19 for the country.
 */
@Data
@Builder
public class CountryCovid19InfoDto {

    /**
     * Country name.
     */
    private String country;

    /**
     * Confirmed cases.
     */
    private Long confirmed;

    /**
     * Recovered cases.
     */
    private Long recovered;

    /**
     * Number of deaths.
     */
    private Long death;

    /**
     * Vaccinated level.
     */
    private BigDecimal vaccinatedLevel;

    /**
     * New confirmed cases.
     */
    private Long newConfirmedCases;

}
