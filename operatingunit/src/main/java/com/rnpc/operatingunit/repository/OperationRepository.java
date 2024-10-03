package com.rnpc.operatingunit.repository;

import com.rnpc.operatingunit.model.Operation;
import io.hypersistence.utils.hibernate.type.basic.Inet;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface OperationRepository extends JpaRepository<Operation, Long> {
    boolean existsByOperatingRoomId(Long id);

    List<Operation> findAllByDate(LocalDate date);

    List<Operation> findAllByDateAndOperatingRoom_Ip(LocalDate date, Inet ip);

    List<Operation> findAllByDateBetweenAndOperatingRoom_Name(LocalDate startDate, LocalDate endDate, String roomName);

    List<Operation> findAllByDateBetweenAndOperationFact_StartTimeIsNotNull(LocalDate startDate, LocalDate endDate);

    List<Operation> findAllByDateAndOperationFact_StartTimeIsNotNullAndOperationFact_EndTimeIsNull(LocalDate date);
}
