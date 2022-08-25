package org.shakh.covid19.client.dto.history;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Map;

@RequiredArgsConstructor
@Slf4j
public class HistoryJsonDeserializer extends JsonDeserializer<HistoryResponse> {

    private final ObjectMapper objectMapper;
    private static final String ALL = "All";

    @Override
    public HistoryResponse deserialize(final JsonParser jsonParser,
                                       final DeserializationContext deserializationContext) throws IOException {
        log.debug("Start deserialization historical information by COVID-19.");

        ObjectCodec oc = jsonParser.getCodec();
        JsonNode node = oc.readTree(jsonParser);

        log.debug("Object for deserialization {}.", node);

        JsonNode allJsonNode = node.get(ALL);
        Map<LocalDate, Long> dates =
                objectMapper.convertValue(allJsonNode.get("dates"), new TypeReference<>() {});
        var historyResponse = new HistoryResponse(dates);

        log.debug("End deserialization historical information by COVID-19 {}.", historyResponse);
        return historyResponse;
    }

}
