package com.rnpc.operatingunit.repository;

import com.rnpc.operatingunit.enums.OperatingRoomStatus;
import com.rnpc.operatingunit.model.OperatingRoom;
import io.hypersistence.utils.hibernate.type.basic.Inet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface OperatingRoomRepository extends JpaRepository<OperatingRoom, Long> {
    Optional<OperatingRoom> findByNameIgnoreCase(String name);

    Optional<OperatingRoom> findByIp(Inet ip);

    List<OperatingRoom> findByStatus(OperatingRoomStatus status);

    @Query("SELECT EXISTS( SELECT 1 FROM Operation o WHERE o.operatingRoom.id=:id )")
    boolean wasUsed(@Param("id") Long id);

    @Query("SELECT oroom FROM OperatingRoom oroom WHERE NOT EXISTS (" +
            "SELECT 1 FROM OperationFact ofact JOIN Operation o ON ofact.id = o.operationFact.id " +
            "WHERE ofact.startTime > :start AND ofact.endTime < :end " +
            "AND o.operatingRoom.id = oroom.id)")
    List<OperatingRoom> findFreeRooms(LocalDateTime start, LocalDateTime end);
}
