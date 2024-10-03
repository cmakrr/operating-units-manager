package com.rnpc.operatingunit.controller;

import com.rnpc.operatingunit.dto.response.operation.OperationFullInfoResponse;
import com.rnpc.operatingunit.dto.response.operation.OperationShortInfoResponse;
import com.rnpc.operatingunit.dto.response.operation.OperationStepStatusResponse;
import com.rnpc.operatingunit.model.Operation;
import com.rnpc.operatingunit.model.OperationStepStatus;
import com.rnpc.operatingunit.service.OperationFactService;
import com.rnpc.operatingunit.service.OperationService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/api/v1/operations")
@RequiredArgsConstructor
public class OperationController {
    private final ModelMapper modelMapper;
    private final OperationService operationService;
    private final OperationFactService operationFactService;

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public OperationFullInfoResponse getOperationsByDate(@PathVariable Long id) {
        Operation operation = operationService.getById(id);

        OperationFullInfoResponse operationResponse = modelMapper.map(operation, OperationFullInfoResponse.class);

        if (Objects.nonNull(operation.getOperationFact())) {
            OperationStepStatus step = operationFactService.getCurrentStep(operation.getOperationFact().getId());
            if (Objects.nonNull(step)) {
                operationResponse.getOperationFact().setCurrentStep(
                        modelMapper.map(step, OperationStepStatusResponse.class));
            }
        }

        return operationResponse;
    }

    @GetMapping("/shortInfo/{id}")
    @ResponseStatus(HttpStatus.OK)
    public OperationShortInfoResponse getOperationShortInfo(@PathVariable Long id) {
        return modelMapper.map(operationService.getById(id), OperationShortInfoResponse.class);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<OperationFullInfoResponse> getOperationsByDate(
            @RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        List<Operation> operations = operationService.getAllByDate(date);

        return modelMapper.map(operations, new TypeToken<List<OperationFullInfoResponse>>() {}.getType());
    }

    @GetMapping("/ongoing")
    @ResponseStatus(HttpStatus.OK)
    public List<OperationFullInfoResponse> getOngoingOperations(
            @RequestParam("startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam("endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        List<Operation> operations = operationService.getOngoingOperationsByDates(startDate, endDate);
        return modelMapper.map(operations, new TypeToken<List<OperationFullInfoResponse>>() {}.getType());
    }

    @GetMapping("/current")
    @ResponseStatus(HttpStatus.OK)
    public List<OperationFullInfoResponse> getCurrentOperations() {
        List<Operation> operations = operationService.getCurrent();
        return modelMapper.map(operations, new TypeToken<List<OperationFullInfoResponse>>() {}.getType());
    }

}
