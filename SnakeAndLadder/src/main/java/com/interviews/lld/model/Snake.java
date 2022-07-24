package com.interviews.lld.model;

import lombok.Getter;
import lombok.ToString;

@ToString
@Getter
public class Snake {

    private final Integer start;

    private final Integer end;

    public Snake(Integer start, Integer end) {
        this.start = start;
        this.end = end;
    }
}
