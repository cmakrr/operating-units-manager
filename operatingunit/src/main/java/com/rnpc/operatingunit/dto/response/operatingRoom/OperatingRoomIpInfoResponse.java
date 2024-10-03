package com.rnpc.operatingunit.dto.response.operatingRoom;

import io.hypersistence.utils.hibernate.type.basic.Inet;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OperatingRoomIpInfoResponse extends OperatingRoomShortInfoResponse {
    private Inet ip;
}
