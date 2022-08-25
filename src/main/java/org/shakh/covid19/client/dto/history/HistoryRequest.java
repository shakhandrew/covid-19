package org.shakh.covid19.client.dto.history;

/**
 * Request object.
 * Historical information on covid cases.
 *
 * @param country Country name.
 * @param status  History status.
 */
public record HistoryRequest(String country, HistoryStatus status) {

}
