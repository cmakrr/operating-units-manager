package com.rnpc.operatingunit.exception.plan;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
public class InvalidOperationPlanDateException extends RuntimeException {
    private LocalDate invalidDate;
    private LocalDate firstValidDate;
}
