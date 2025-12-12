package org.bf.reportservice.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ReportCategory {

    BLOCKED_PATH("보행로/도로 장애물", 50),
    RAMP_OBSTACLE("경사로 문제", 10),
    CURB_OR_STAIR("단차/계단 문제", 20),
    ETC("기타", 10);

    private final String description;
    private final int point;
}
