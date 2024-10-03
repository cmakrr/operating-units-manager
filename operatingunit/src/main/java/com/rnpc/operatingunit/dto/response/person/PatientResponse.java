package com.rnpc.operatingunit.dto.response.person;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PatientResponse {
    private Long id;
    private String fullName;
    private int age;
    private String birthYear;
    private int roomNumber;
}
