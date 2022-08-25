package org.shakh.covid19.client.dto.history;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.shakh.covid19.client.error.Covid19DataNotFound;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class HistoryJsonDeserializerTest {

    private HistoryJsonDeserializer deserializer;
    private ObjectMapper objectMapper;

    @BeforeEach
    void init() {
        objectMapper = new ObjectMapper()
                .registerModule(new JavaTimeModule());
        deserializer = new HistoryJsonDeserializer(objectMapper);
    }

    @Test
    void deserialize() throws IOException {
        LocalDate date = LocalDate.of(2022, 1, 1);
        Map<LocalDate, Long> dates = Map.of(
                date, Long.valueOf(RandomStringUtils.randomNumeric(7)),
                LocalDate.of(2022, 1, 2), Long.valueOf(RandomStringUtils.randomNumeric(7)),
                LocalDate.of(2022, 1, 3), Long.valueOf(RandomStringUtils.randomNumeric(7))
        );

        String json = getDates(dates);
        JsonParser parser = objectMapper.createParser(json);

        HistoryResponse res = deserializer.deserialize(parser, null);

        assertNotNull(res);
        assertEquals(dates.size(), res.dates().size());
        assertTrue(res.dates().containsKey(date));
    }

    @Test()
    void deserializeDataNotFound() throws IOException {
        String json = "{}";
        JsonParser parser = objectMapper.createParser(json);

        Assertions.assertThrows(Covid19DataNotFound.class,
                () -> deserializer.deserialize(parser, null));
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
