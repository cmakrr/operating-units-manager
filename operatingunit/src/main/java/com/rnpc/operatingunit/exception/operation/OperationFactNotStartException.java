package com.rnpc.operatingunit.exception.operation;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
public class OperationFactNotStartException extends RuntimeException {
    private Long operationFactId;
}
