package org.shakh.covid19.client.dto.vaccines;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Builder;
import lombok.Data;

/**
 * Response object.
 * The information about vaccines of covid19.
 */
@Data
@Builder
@JsonDeserialize(using = VaccinesJsonDeserializer.class)
public class VaccinesResponse {

    /**
     * The number of vaccinated people.
     */
    private Long peopleVaccinated;

    /**
     * The number of population.
     */
    private Long population;

}
