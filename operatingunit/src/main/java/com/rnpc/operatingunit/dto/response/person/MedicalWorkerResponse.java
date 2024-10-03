package com.rnpc.operatingunit.dto.response.person;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MedicalWorkerResponse {
    private Long id;
    private String fullName;
    private String role;
}
