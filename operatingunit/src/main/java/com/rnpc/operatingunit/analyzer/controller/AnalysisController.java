package com.rnpc.operatingunit.analyzer.controller;

import com.rnpc.operatingunit.analyzer.operatingrooms.OperatingRoomAnalyzerService;
import com.rnpc.operatingunit.analyzer.operations.OperationsAnalyzerService;
import com.rnpc.operatingunit.analyzer.workers.WorkersAnalyzerService;
import com.rnpc.operatingunit.dto.response.operation.OperationsAnalysisInfo;
import com.rnpc.operatingunit.dto.response.operation.OperationsOverallAnalysis;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/analysis")
public class AnalysisController {
    private final OperationsAnalyzerService operationsAnalyzerService;
    private final OperatingRoomAnalyzerService operatingRoomAnalyzerService;
    private final WorkersAnalyzerService workersAnalyzerService;

    @PostMapping("/worker/{id}")
    public OperationsAnalysisInfo getWorkerOperationsAnalysis(@PathVariable Long id, @RequestBody DateRangeRequest request) {
        return workersAnalyzerService.createAnalysisInfo(id, request.getStartDate(), request.getEndDate());
    }

    @PostMapping("/worker/overall/{id}")
    public OperationsOverallAnalysis getWorkerOverallAnalysis(@PathVariable Long id, @RequestBody DateRangeRequest request) {
        return workersAnalyzerService.createOverallAnalysis(id, request.getStartDate(), request.getEndDate());
    }

    @PostMapping("/room/{name}")
    public OperationsAnalysisInfo getOperationRoomOperationsAnalysis(@PathVariable String name, @RequestBody DateRangeRequest request) {
        return operatingRoomAnalyzerService.createAnalysisInfo(name, request.getStartDate(), request.getEndDate());
    }

    @PostMapping("/room/overall/{name}")
    public OperationsOverallAnalysis getOperationRoomOverallAnalysis(@PathVariable String name, @RequestBody DateRangeRequest request) {
        return operatingRoomAnalyzerService.createOverallAnalysis(name, request.getStartDate(), request.getEndDate());
    }

    @PostMapping
    public OperationsAnalysisInfo getOperationsAnalysis(@RequestBody DateRangeRequest request) {
        return operationsAnalyzerService.createOperationsAnalysisInfo(request.getStartDate(), request.getEndDate());
    }

    @PostMapping("/overall")
    public OperationsOverallAnalysis getOperationsOverallAnalysis(@RequestBody DateRangeRequest request) {
        return operationsAnalyzerService.createOperationsOverallAnalysis(request.getStartDate(), request.getEndDate());
    }
}
