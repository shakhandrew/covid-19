package org.shakh.covid19.client.dto.history;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum HistoryStatus {

    CONFIRMED("confirmed"),

    DEATH("deaths");

    private final String name;

}