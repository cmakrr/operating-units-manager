package com.rnpc.operatingunit.dto.response.operation;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class OperationsAnalysisInfo {
    private Map<LocalDate, List<OperationInfo>> operations;
    private LocalDate analysisStart;
    private LocalDate analysisEnd;
}
