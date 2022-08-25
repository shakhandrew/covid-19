package org.shakh.covid19.client.dto.history;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.time.LocalDate;
import java.util.Map;

/**
 * Response object.
 * Historical information on covid19 cases.
 *
 * @param dates Map date and cases.
 */
@JsonDeserialize(using = HistoryJsonDeserializer.class)
public record HistoryResponse(Map<LocalDate, Long> dates) {

}
