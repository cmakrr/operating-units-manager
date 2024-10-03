package com.rnpc.operatingunit.service;

import io.hypersistence.utils.hibernate.type.basic.Inet;

public interface OperatingRoomAccessService {
    void checkOperatingRoomAccess(Inet roomIp, String ip);

    void checkOperatingRoomAccess(Long operationId, String ip);
}
