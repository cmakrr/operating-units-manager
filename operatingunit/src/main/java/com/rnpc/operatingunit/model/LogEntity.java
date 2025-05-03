package com.rnpc.operatingunit.model;

import com.rnpc.operatingunit.enums.LogAffectedEntityType;
import com.rnpc.operatingunit.enums.LogOperationType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;

@Entity
@Data
public class LogEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LogOperationType operationType;
    private LogAffectedEntityType affectedEntityType;
    @CreationTimestamp
    private LocalDateTime logTime;
    private String username;
}
