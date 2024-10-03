package com.rnpc.operatingunit.repository;

import com.rnpc.operatingunit.model.OperatingRoom;
import io.hypersistence.utils.hibernate.type.basic.Inet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface OperatingRoomRepository extends JpaRepository<OperatingRoom, Long> {
    Optional<OperatingRoom> findByNameIgnoreCase(String name);

    Optional<OperatingRoom> findByIp(Inet ip);

    @Query("SELECT EXISTS( SELECT 1 FROM Operation o WHERE o.operatingRoom.id=:id )")
    boolean wasUsed(@Param("id") Long id);
}
