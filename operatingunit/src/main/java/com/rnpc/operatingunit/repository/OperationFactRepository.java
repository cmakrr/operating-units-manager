package com.rnpc.operatingunit.repository;

import com.rnpc.operatingunit.model.MedicalWorker;
import com.rnpc.operatingunit.model.OperationFact;
import jakarta.persistence.CascadeType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface OperationFactRepository extends JpaRepository<OperationFact, Long> {
    @Query("SELECT e.of_id FROM operation_fact e WHERE e.of_start_time BETWEEN :startDate AND :endDate AND e.of_end_time > :endDate")
    List<Long> findIdsBetweenDates(@Param("startDate") LocalDateTime startDate,
                                         @Param("endDate") LocalDateTime endDate);

    @Query("SELECT e.of_id FROM operation_fact e WHERE e.of_start_time BETWEEN :startDate AND :endDate AND e.of_end_time > :endDate " +
            "AND (e.of_operator_id = :workerId OR e.of_assistant_id = :workerId OR e.of_transfusiologist_id = :workerId )" )
    List<Long> findIdsBetweenDatesWithWorker(@Param("startDate") LocalDateTime startDate,
                                            @Param("endDate") LocalDateTime endDate,
                                             @Param("workerId") Long workerId);
}
