package com.rnpc.operatingunit.repository;

import com.rnpc.operatingunit.model.Operation;
import io.hypersistence.utils.hibernate.type.basic.Inet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface OperationRepository extends JpaRepository<Operation, Long> {
    boolean existsByOperatingRoomId(Long id);

    List<Operation> findAllByIdIn(List<Long> ids);

    List<Operation> findAllByDate(LocalDate date);

    List<Operation> findAllByDateAndOperatingRoom_Ip(LocalDate date, Inet ip);

    List<Operation> findAllByDateBetweenAndOperatingRoom_Name(LocalDate startDate, LocalDate endDate, String roomName);

    List<Operation> findAllByDateBetweenAndOperationFact_StartTimeIsNotNull(LocalDate startDate, LocalDate endDate);

    List<Operation> findAllByDateAndOperationFact_StartTimeIsNotNullAndOperationFact_EndTimeIsNull(LocalDate date);

    List<Operation> findByDateBetween(LocalDate startDate, LocalDate endDate);

    @Query("SELECT e FROM operations e WHERE e.o_date BETWEEN :startDate AND :endDate AND e.o_operation_room_id = :roomId" )
    List<Operation> findBetweenDatesInOperatingRoom(@Param("startDate") LocalDate startDate,
                                             @Param("endDate") LocalDate endDate,
                                                    @Param("roomId") Long roomId);
}
