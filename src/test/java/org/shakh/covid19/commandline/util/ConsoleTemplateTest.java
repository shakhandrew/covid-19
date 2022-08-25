package org.shakh.covid19.commandline.util;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.Test;
import org.shakh.covid19.service.dto.CountryCovid19InfoDto;

import java.lang.reflect.Field;
import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class ConsoleTemplateTest {

    @Test
    void getCovid19InfoForCountry() throws IllegalAccessException, NoSuchFieldException {
        var info = CountryCovid19InfoDto.builder()
                .country(RandomStringUtils.randomAlphanumeric(5))
                .confirmed(Long.valueOf(RandomStringUtils.randomNumeric(5)))
                .recovered(Long.valueOf(RandomStringUtils.randomNumeric(5)))
                .death(Long.valueOf(RandomStringUtils.randomNumeric(5)))
                .vaccinatedLevel(BigDecimal.ZERO)
                .newConfirmedCases(Long.valueOf(RandomStringUtils.randomNumeric(5)))
                .build();

        Field templateField = ConsoleTemplate.class.getDeclaredField("COVID19_INFO_FOR_COUNTRY_TEMPLATE");
        templateField.setAccessible(true);
        String template = String.valueOf(templateField.get(null));

        String result = ConsoleTemplate.getCovid19InfoForCountry(info);

        assertNotNull(result);
        assertEquals(
                String.format(template,
                        info.getCountry(),
                        info.getConfirmed(),
                        info.getRecovered(),
                        info.getDeath(),
                        info.getVaccinatedLevel(),
                        info.getNewConfirmedCases()),
                result
        );
    }

}
