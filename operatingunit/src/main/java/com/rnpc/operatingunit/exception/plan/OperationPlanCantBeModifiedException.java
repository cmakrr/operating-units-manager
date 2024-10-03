package com.rnpc.operatingunit.exception.plan;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Setter
@Getter
@AllArgsConstructor
public class OperationPlanCantBeModifiedException extends RuntimeException {
    private LocalDate planDate;
}
