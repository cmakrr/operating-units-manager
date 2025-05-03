package com.rnpc.operatingunit.repository;

import com.rnpc.operatingunit.model.LogEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface LogRepository extends JpaRepository<LogEntity, Long> {
    @Query("SELECT l FROM LogEntity l WHERE l.logTime BETWEEN :startDate AND :endDate")
    List<LogEntity> findByDateBetween(@Param("startDate") LocalDateTime startDate,
                                      @Param("endDate") LocalDateTime endDate);
}
