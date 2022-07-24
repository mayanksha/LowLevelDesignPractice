package com.interviews.lld.model;

import lombok.Getter;
import lombok.ToString;

@ToString
@Getter
public class Ladder {
    private final Integer start;

    private final Integer end;

    public Ladder(Integer start, Integer end) {
        this.start = start;
        this.end = end;
    }
}
