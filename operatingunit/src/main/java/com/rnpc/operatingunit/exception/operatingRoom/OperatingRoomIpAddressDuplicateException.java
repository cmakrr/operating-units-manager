package com.rnpc.operatingunit.exception.operatingRoom;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
public class OperatingRoomIpAddressDuplicateException extends RuntimeException {
    private String ipAddress;
    private String operatingRoomNameWithIp;
    private String operatingRoomNameWithInvalidIp;
}