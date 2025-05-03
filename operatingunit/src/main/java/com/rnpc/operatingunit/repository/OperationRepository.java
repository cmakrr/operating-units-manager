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

    @Query("SELECT e FROM Operation e WHERE e.operationFact.id IN :ids" )
    List<Operation> findAllByFactsId(List<Long> ids);

    List<Operation> findAllByDate(LocalDate date);

    List<Operation> findAllByDateAndOperatingRoom_Ip(LocalDate date, Inet ip);

    List<Operation> findAllByDateBetweenAndOperatingRoom_Name(LocalDate startDate, LocalDate endDate, String roomName);

    List<Operation> findAllByDateBetweenAndOperationFact_StartTimeIsNotNull(LocalDate startDate, LocalDate endDate);

    List<Operation> findAllByDateAndOperationFact_StartTimeIsNotNullAndOperationFact_EndTimeIsNull(LocalDate date);


    @Query("SELECT e FROM Operation e WHERE e.operationFact.endTime BETWEEN :startDate AND :endDate" )
    List<Operation> findByDateBetween(@Param("startDate") LocalDateTime startDate,
                                      @Param("endDate") LocalDateTime endDate);

    @Query("SELECT e FROM Operation e WHERE e.operationFact.endTime BETWEEN :startDate AND :endDate AND e.operatingRoom.id= :roomId AND e.operationFact is not null" )
    List<Operation> findBetweenDatesInOperatingRoom(@Param("startDate") LocalDateTime startDate,
                                             @Param("endDate") LocalDateTime endDate,
                                                    @Param("roomId") Long roomId);
}
