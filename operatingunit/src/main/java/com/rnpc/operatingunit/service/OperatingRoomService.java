package com.rnpc.operatingunit.service;

import com.rnpc.operatingunit.enums.OperatingRoomStatus;
import com.rnpc.operatingunit.model.OperatingRoom;

import java.time.LocalDateTime;
import java.util.List;

public interface OperatingRoomService {
    OperatingRoom saveOrGetOperatingRoom(OperatingRoom operatingRoom);

    List<OperatingRoom> getOperatingRooms();

    String getOperatingRoomNameByIp(String ip);

    OperatingRoom setOperatingRoomIpAddress(Long id, String ip);

    List<OperatingRoom> findByStatus(OperatingRoomStatus status);

    List<OperatingRoom> findFreeRooms(LocalDateTime start, LocalDateTime end);

    OperatingRoom create(String name, String ip);

    void delete(Long id);
}
