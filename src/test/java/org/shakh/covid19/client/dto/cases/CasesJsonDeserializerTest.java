package org.shakh.covid19.client.dto.cases;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.shakh.covid19.client.error.Covid19DataNotFound;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class CasesJsonDeserializerTest {

    private CasesJsonDeserializer deserializer;
    private ObjectMapper objectMapper;

    @BeforeEach
    void init() {
        objectMapper = new ObjectMapper();
        deserializer = new CasesJsonDeserializer();
    }

    @Test
    void deserialize() throws IOException {
        Long confirmed = Long.valueOf(RandomStringUtils.randomNumeric(5));
        Long recovered = Long.valueOf(RandomStringUtils.randomNumeric(5));
        Long deaths = Long.valueOf(RandomStringUtils.randomNumeric(5));
        String country = RandomStringUtils.randomAlphanumeric(5);

        String json = getCase(confirmed, recovered, deaths, country);
        JsonParser parser = objectMapper.createParser(json);

        CasesResponse res = deserializer.deserialize(parser, null);

        assertNotNull(res);
        assertEquals(confirmed, res.getConfirmed());
        assertEquals(recovered, res.getRecovered());
        assertEquals(deaths, res.getDeaths());
        assertEquals(country, res.getCountry());
    }

    @Test()
    void deserializeDataNotFound() throws IOException {
        String json = "{}";
        JsonParser parser = objectMapper.createParser(json);

        Assertions.assertThrows(Covid19DataNotFound.class,
                () -> deserializer.deserialize(parser, null));
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

}
