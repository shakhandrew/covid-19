package org.shakh.covid19.commandline.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Console command.
 */
@RequiredArgsConstructor
@Getter
public enum Command {

    EXIT("exit");

    private final String name;

}
