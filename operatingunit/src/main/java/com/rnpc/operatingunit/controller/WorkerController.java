package com.rnpc.operatingunit.controller;

import com.rnpc.operatingunit.model.MedicalWorker;
import com.rnpc.operatingunit.model.WorkerStatus;
import com.rnpc.operatingunit.service.MedicalWorkerService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("api/v1/worker")
public class WorkerController {
    private final MedicalWorkerService workerService;

    @GetMapping("/all/employed")
    public List<MedicalWorker> findAll() {
        return workerService.findAllEmployedWorkers();
    }

    @GetMapping("/status/{status}")
    public List<MedicalWorker> findByStatus(@PathVariable WorkerStatus status) {
        return workerService.findByStatus(status);
    }

    @GetMapping("/name/{name}")
    public List<MedicalWorker> findByName(@PathVariable String name) {
        return workerService.findByFullNameContaining(name);
    }

    @PutMapping("/update")
    public void updateMedicalWorker(@RequestBody MedicalWorker medicalWorker) {
        workerService.save(medicalWorker);
    }

    @PostMapping("/save")
    public void save(@RequestBody MedicalWorker medicalWorker) {
        workerService.save(medicalWorker);
    }
}
