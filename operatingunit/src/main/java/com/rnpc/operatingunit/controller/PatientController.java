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
    public List<Patient> getAllPatientsInHospital(){
        return patientService.findAllPatientsInHospital();
    }

    @GetMapping("/name/{name}")
    public List<Patient> findByName(@PathVariable String name){
        return patientService.findByFullNameContaining(name);
    }

    @PutMapping
    public void updatePatient(@RequestBody Patient patient){
        patientService.savePatient(patient);
    }

    @PutMapping("/dispatch/{id}")
    public void dispatchPatient(@PathVariable Long id){
        patientService.dispatchPatient(id);
    }

    @PostMapping
    public void savePatient(@RequestBody Patient patient){
        patientService.savePatient(patient);
    }
}
