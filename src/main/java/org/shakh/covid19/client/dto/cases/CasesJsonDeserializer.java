package org.shakh.covid19.client.dto.cases;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

@RequiredArgsConstructor
@Slf4j
public class CasesJsonDeserializer extends JsonDeserializer<CasesResponse> {

    private static final String ALL = "All";

    @Override
    public CasesResponse deserialize(final JsonParser jsonParser,
                                     final DeserializationContext deserializationContext) throws IOException {
        log.debug("Start deserialization information about cases of COVID-19.");

        ObjectCodec oc = jsonParser.getCodec();
        JsonNode node = oc.readTree(jsonParser);

        log.debug("Object for deserialization {}.", node);

        JsonNode allJsonNode = node.get(ALL);

        var casesResponse = CasesResponse.builder()
                .confirmed(allJsonNode.get("confirmed").asLong())
                .recovered(allJsonNode.get("recovered").asLong())
                .deaths(allJsonNode.get("deaths").asLong())
                .country(allJsonNode.get("country").asText())
                .build();

        log.debug("End deserialization information about cases of COVID-19 {}.", casesResponse);
        return casesResponse;
    }

}
