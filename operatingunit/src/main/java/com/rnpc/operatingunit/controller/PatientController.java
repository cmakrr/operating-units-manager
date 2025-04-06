package com.rnpc.operatingunit.controller;

import com.rnpc.operatingunit.model.Patient;
import com.rnpc.operatingunit.service.PatientService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("api/v1/patient")
public class PatientController {
    private final PatientService patientService;

    @GetMapping("/all")
    public List<Patient> getAll(){
        return patientService.findAll();
    }

    @GetMapping("/name/{name}")
    public List<Patient> findByName(@PathVariable String name){
        return patientService.findByFullNameContaining(name);
    }

    @PutMapping
    public void updatePatient(@RequestBody Patient patient){
        patientService.savePatient(patient);
    }

    @PostMapping
    public void savePatient(@RequestBody Patient patient){
        patientService.savePatient(patient);
    }
}
