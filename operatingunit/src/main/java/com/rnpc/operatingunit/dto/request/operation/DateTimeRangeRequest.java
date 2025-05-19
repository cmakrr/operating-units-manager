package com.rnpc.operatingunit.dto.request.operation;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class DateTimeRangeRequest {
    private LocalDateTime start;
    private LocalDateTime end;
}
