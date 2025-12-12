package org.bf.reportservice.domain.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ImageTag {

    CONSTRUCTION("construction", 50),
    TREE("tree", 20),
    ROCK("rock", 20),
    FURNITURE("furniture", 20),
    SLOPE("slope", 10),
    OTHER_OBSTACLE("other_obstacle", 10);

    private final String code;
    private final int point;

    public static ImageTag from(String code) {
        for (ImageTag tag : ImageTag.values()) {
            if (tag.code.equalsIgnoreCase(code)) {
                return tag;
            }
        }
        throw new IllegalArgumentException("Unknown tag code " + code);
    }
}
