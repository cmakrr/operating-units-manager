package com.rnpc.operatingunit.parser.impl;

import com.rnpc.operatingunit.model.Patient;
import com.rnpc.operatingunit.parser.PatientDetailsParser;
import com.rnpc.operatingunit.repository.PatientRepository;
import com.rnpc.operatingunit.service.PatientService;
import com.rnpc.operatingunit.service.PersonService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.rnpc.operatingunit.parser.constants.OperationPlanParserConstants.Regexp.DIGIT_REGEX;

@Component
@RequiredArgsConstructor
public class DefaultPatientDetailsParser implements PatientDetailsParser {
    private static final Pattern digitPattern = Pattern.compile(DIGIT_REGEX);
    private static final String INVALID_MAX_AGE_MESSAGE = "Minimum age cannot be negative";
    private final PatientRepository patientRepository;
    private final PersonService personService;

    @Value("${patient.age.max}")
    private int maxAge;

    @PostConstruct
    private void postConstruct() {
        if (maxAge < 0) {
            throw new IllegalArgumentException(INVALID_MAX_AGE_MESSAGE);
        }
    }

    @Override
    public void updatePatientFromDetails(Patient patient, String details) {
        String fullName = details;
        Optional<Integer> ageValue = getDigitSequence(details);
        if (ageValue.isPresent()) {
            Integer age = ageValue.get();
            setPatientAge(patient, age);

            fullName = details.substring(0, details.indexOf(age.toString()));
        }

        patient.setFullName(personService.getFullName(fullName));
    }

    @Override
    public void setPatientRoomNumber(Patient patient, String roomNumberValue) {
        getDigitSequence(roomNumberValue).ifPresent(patient::setRoomNumber);
    }

    @Override
    public Optional<Patient> tryFindByFullName(String fullName) {
        Optional<Patient> result;
        try{
            Long id = Long.parseLong(fullName);
            result = patientRepository.findById(id);
        } catch (Exception e){
            result = Optional.empty();
        }
        if(result.isEmpty()){
            result = patientRepository.findByFullName(fullName);
        }
        return result;
    }

    private void setPatientAge(Patient patient, Integer age) {
        if (age < maxAge) {
            patient.setAge(age);
        } else {
            patient.setBirthYear(LocalDate.of(age, 1, 1));
        }
    }

    private Optional<Integer> getDigitSequence(String text) {
        Matcher digitMatcher = digitPattern.matcher(text);
        if (digitMatcher.find()) {
            return Optional.of(Integer.parseInt(digitMatcher.group()));
        }

        return Optional.empty();
    }
}
