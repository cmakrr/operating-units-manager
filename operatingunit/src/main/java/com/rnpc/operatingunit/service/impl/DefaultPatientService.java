package com.rnpc.operatingunit.service.impl;

import com.rnpc.operatingunit.enums.PatientStatus;
import com.rnpc.operatingunit.model.Patient;
import com.rnpc.operatingunit.repository.PatientRepository;
import com.rnpc.operatingunit.service.PatientService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class DefaultPatientService implements PatientService {
    private final PatientRepository patientRepository;

    public Patient saveOrGetPatient(Patient patient) {
        if (Objects.nonNull(patient)) {
            Example<Patient> patientExample = Example.of(populatePatientWithoutId(patient));

            Optional<Patient> pat = patientRepository.findOne(patientExample);

            if (pat.isPresent()) {
                return pat.get();
            }

            patientRepository.save(patient);
            log.info("Patient [{}] was saved", patient.getFullName());
        }

        return patient;
    }

    @Override
    public void savePatient(Patient patient) {
        patientRepository.save(patient);
    }

    @Override
    public Patient getPatient(Long id) {
        return patientRepository.findById(id).get();
    }

    @Override
    public List<Patient> findAllPatientsInHospital() {
        return patientRepository.findByStatus(PatientStatus.IN_HOSPITAL);
    }



    @Override
    public List<Patient> findByFullNameContaining(String name) {
        return patientRepository.findByFullNameContaining(name);
    }

    @Override
    public void dispatchPatient(Long id) {
        Patient patient = patientRepository.findById(id).get();
        patient.setStatus(PatientStatus.OUT_OF_HOSPITAL);
        patientRepository.save(patient);
    }

    public int getPatientAgeOrBirthYear(Patient patient) {
        int age = patient.getAge();
        LocalDate birthYear = patient.getBirthYear();

        return age > 0 ? age : birthYear.getYear();
    }

    private Patient populatePatientWithoutId(Patient patient) {
        Patient patientExample = new Patient();

        patientExample.setFullName(patient.getFullName());
        patientExample.setRoomNumber(patient.getRoomNumber());
        patientExample.setBirthYear(patient.getBirthYear());
        patientExample.setAge(patient.getAge());

        return patientExample;
    }
}
