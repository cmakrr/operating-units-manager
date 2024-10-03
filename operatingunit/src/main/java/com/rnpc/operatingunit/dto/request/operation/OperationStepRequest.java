package com.rnpc.operatingunit.dto.request.operation;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class OperationStepRequest {
    @Nullable
    @Size(max = 255)
    private String comment;
}
