package com.rnpc.operatingunit.dto.response.operation;

import com.rnpc.operatingunit.dto.response.operatingRoom.OperatingRoomShortInfoResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OperationFullInfoResponse extends OperationResponse {
    private OperatingRoomShortInfoResponse operatingRoom;
}
