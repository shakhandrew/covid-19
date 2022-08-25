package org.shakh.covid19.client.dto.cases;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Builder;
import lombok.Data;

/**
 * Response object.
 * The information about cases of covid19.
 */
@Data
@Builder
@JsonDeserialize(using = CasesJsonDeserializer.class)
public class CasesResponse {

    /**
     * Confirmed cases.
     */
    private Long confirmed;

    /**
     * Recovered cases.
     */
    private Long recovered;

    /**
     * Death cases.
     */
    private Long deaths;

    /**
     * Country name.
     */
    private String country;

}
