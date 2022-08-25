package org.shakh.covid19.client.dto.vaccines;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.shakh.covid19.client.error.Covid19DataNotFound;

import java.io.IOException;

@RequiredArgsConstructor
@Slf4j
public class VaccinesJsonDeserializer extends JsonDeserializer<VaccinesResponse> {

    private static final String ALL = "All";

    @Override
    public VaccinesResponse deserialize(final JsonParser jsonParser,
                                        final DeserializationContext deserializationContext) throws IOException {
        log.debug("Start deserialization vaccines information by COVID-19.");

        ObjectCodec oc = jsonParser.getCodec();
        JsonNode node = oc.readTree(jsonParser);

        log.debug("Object for deserialization {}.", node);

        JsonNode allJsonNode = node.get(ALL);

        if (allJsonNode == null) {
            throw new Covid19DataNotFound("Can't deserialize object. Data not found.");
        }

        var vaccinesResponse =
                VaccinesResponse.builder()
                        .peopleVaccinated(allJsonNode.get("people_vaccinated").longValue())
                        .population(allJsonNode.get("population").longValue())
                        .build();

        log.debug("End deserialization vaccines information by COVID-19 {}.", vaccinesResponse);
        return vaccinesResponse;
    }

}
