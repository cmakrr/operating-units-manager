package com.rnpc.operatingunit.service;

import com.rnpc.operatingunit.model.OperatingRoom;

import java.util.List;

public interface OperatingRoomService {
    OperatingRoom saveOrGetOperatingRoom(OperatingRoom operatingRoom);

    List<OperatingRoom> getOperatingRooms();

    String getOperatingRoomNameByIp(String ip);

    OperatingRoom setOperatingRoomIpAddress(Long id, String ip);

    OperatingRoom create(String name, String ip);

    void delete(Long id);
}
