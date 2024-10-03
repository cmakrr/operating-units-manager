package com.rnpc.operatingunit.exception.operatingRoom;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OperatingRoomManageException extends RuntimeException {
    private String currentOperationName;
}
