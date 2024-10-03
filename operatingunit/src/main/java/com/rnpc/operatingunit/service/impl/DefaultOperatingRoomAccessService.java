package com.rnpc.operatingunit.service.impl;

import com.rnpc.operatingunit.exception.operatingRoom.OperatingRoomUnauthorizedAction;
import com.rnpc.operatingunit.service.OperatingRoomAccessService;
import com.rnpc.operatingunit.service.OperationService;
import io.hypersistence.utils.hibernate.type.basic.Inet;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@RequiredArgsConstructor
@Slf4j
public class DefaultOperatingRoomAccessService implements OperatingRoomAccessService {
    private static final String UNAVAILABLE_OPERATION =
            "Данная операция недоступна, так как проводится в другом операционном блоке";
    private static final String NOT_OPERATING_ROOM = "Ваш IP-адрес не назначен ни одному операционному блоку";
    private static final String REMOTE_IP = "Remote IP = %s";

    private final OperationService operationService;

    public void checkOperatingRoomAccess(Inet roomIp, String ip) {
        checkAccess(roomIp, ip);
    }

    public void checkOperatingRoomAccess(Long operationId, String ip) {
        Inet roomIp = operationService.getById(operationId).getOperatingRoom().getIp();

        checkAccess(roomIp, ip);
    }

    private void checkAccess(Inet roomIp, String ip) {
        log.info(String.format(REMOTE_IP, ip));

        if (Objects.isNull(roomIp)) {
            throw new OperatingRoomUnauthorizedAction(NOT_OPERATING_ROOM);
        }
        if (!StringUtils.equalsIgnoreCase(roomIp.getAddress(), ip)) {
            throw new OperatingRoomUnauthorizedAction(UNAVAILABLE_OPERATION);
        }
    }
}
