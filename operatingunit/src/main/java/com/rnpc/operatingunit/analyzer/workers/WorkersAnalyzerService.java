package com.rnpc.operatingunit.analyzer.workers;

import com.rnpc.operatingunit.repository.MedicalWorkerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class WorkersAnalyzerService {
    private final MedicalWorkerRepository medicalWorkerRepository;
}
