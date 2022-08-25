package org.shakh.covid19.commandline.util;

import org.shakh.covid19.service.dto.CountryCovid19InfoDto;

/**
 * Class for formatting text for output to the console.
 */
public final class ConsoleTemplate {

    /**
     * Template for country information about covid19.
     */
    private static final String COVID19_INFO_FOR_COUNTRY_TEMPLATE =
            """
                    Information on COVID-19 by %s:
                     - confirmed: %s
                     - recovered: %s
                     - deaths: %s
                     - vaccinated level: %s%%
                     - new confirmed cases: %s.
                    """;

    private ConsoleTemplate() {
        throw new UnsupportedOperationException();
    }

    /**
     * Getting information for country about covid19.
     */
    public static String getCovid19InfoForCountry(final CountryCovid19InfoDto countryCovidInfoDto) {
        return String.format(COVID19_INFO_FOR_COUNTRY_TEMPLATE,
                countryCovidInfoDto.getCountry(),
                countryCovidInfoDto.getConfirmed(),
                countryCovidInfoDto.getRecovered(),
                countryCovidInfoDto.getDeath(),
                countryCovidInfoDto.getVaccinatedLevel(),
                countryCovidInfoDto.getNewConfirmedCases());
    }

}
