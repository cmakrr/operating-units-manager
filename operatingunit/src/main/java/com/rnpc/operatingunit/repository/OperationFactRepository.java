package com.rnpc.operatingunit.repository;

import com.rnpc.operatingunit.model.OperationFact;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface OperationFactRepository extends JpaRepository<OperationFact, Long> {
    @Query("SELECT e.id FROM OperationFact e WHERE e.startTime BETWEEN :startDate AND :endDate AND e.endTime > :endDate")
    List<Long> findIdsBetweenDates(@Param("startDate") LocalDateTime startDate,
                                         @Param("endDate") LocalDateTime endDate);

    @Query("SELECT e.id FROM OperationFact e WHERE e.startTime BETWEEN :startDate AND :endDate AND e.endTime > :endDate " +
            "AND (e.operator.id = :workerId OR e.assistant.id = :workerId OR e.transfusiologist.id = :workerId )" )
    List<Long> findIdsBetweenDatesWithWorker(@Param("startDate") LocalDateTime startDate,
                                            @Param("endDate") LocalDateTime endDate,
                                             @Param("workerId") Long workerId);
}
