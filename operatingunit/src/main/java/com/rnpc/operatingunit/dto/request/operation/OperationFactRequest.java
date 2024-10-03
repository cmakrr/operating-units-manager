package com.rnpc.operatingunit.dto.request.operation;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class OperationFactRequest {
    @Size(min = 5, max = 255)
    @NotBlank(message = "Оператор должен быть установлен")
    private String operatorName;
    @Size(min = 5, max = 255)
    @NotBlank(message = "Ассистент должен быть установлен")
    private String assistantName;
    @Nullable
    @Size(min = 5, max = 255)
    private String transfusiologistName;
    @Nullable
    @Size(max = 255)
    private String instruments;
}
