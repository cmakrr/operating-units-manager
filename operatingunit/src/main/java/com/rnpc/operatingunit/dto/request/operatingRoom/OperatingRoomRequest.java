package com.rnpc.operatingunit.dto.request.operatingRoom;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class OperatingRoomRequest {
    @NotBlank(message = "Название операционного блока должено быть установлено!")
    private String name;
    @Nullable
    @Size(min = 7, max = 15, message = "Неверное значение IP-адреса!")
    @Pattern(regexp = "^((25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$",
            message = "Неверное значение IP-адреса!")
    private String ipAddress;
}
