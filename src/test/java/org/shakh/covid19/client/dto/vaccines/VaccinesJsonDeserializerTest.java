package org.shakh.covid19.client.dto.vaccines;

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

class VaccinesJsonDeserializerTest {

    private VaccinesJsonDeserializer deserializer;
    private ObjectMapper objectMapper;

    @BeforeEach
    void init() {
        objectMapper = new ObjectMapper();
        deserializer = new VaccinesJsonDeserializer();
    }

    @Test
    void deserialize() throws IOException {
        Long population = Long.valueOf(RandomStringUtils.randomNumeric(5));
        Long peopleVaccinated = Long.valueOf(RandomStringUtils.randomNumeric(5));

        String json = getVaccine(population, peopleVaccinated);
        JsonParser parser = objectMapper.createParser(json);

        VaccinesResponse res = deserializer.deserialize(parser, null);

        assertNotNull(res);
        assertEquals(population, res.getPopulation());
        assertEquals(peopleVaccinated, res.getPeopleVaccinated());
    }

    @Test()
    void deserializeDataNotFound() throws IOException {
        String json = "{}";
        JsonParser parser = objectMapper.createParser(json);

        Assertions.assertThrows(Covid19DataNotFound.class,
                () -> deserializer.deserialize(parser, null));
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

}
