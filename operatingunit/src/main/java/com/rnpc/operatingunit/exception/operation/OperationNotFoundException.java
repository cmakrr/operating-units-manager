package com.rnpc.operatingunit.exception.operation;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class OperationNotFoundException extends RuntimeException {
    private Long operationId;
}
