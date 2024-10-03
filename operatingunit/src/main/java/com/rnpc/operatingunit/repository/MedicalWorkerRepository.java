package com.rnpc.operatingunit.repository;

import com.rnpc.operatingunit.model.MedicalWorker;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MedicalWorkerRepository extends JpaRepository<MedicalWorker, Long> {
    Optional<MedicalWorker> findByFullNameAndRole(String fullName, String role);
}
