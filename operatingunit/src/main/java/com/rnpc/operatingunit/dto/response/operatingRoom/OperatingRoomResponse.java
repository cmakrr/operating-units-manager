package com.rnpc.operatingunit.dto.response.operatingRoom;

import com.rnpc.operatingunit.dto.response.operation.OperationResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OperatingRoomResponse extends OperatingRoomShortInfoResponse {
    private OperationResponse currentOperation;
}
