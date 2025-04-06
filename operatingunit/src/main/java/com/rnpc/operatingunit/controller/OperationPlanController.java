package com.rnpc.operatingunit.controller;

import com.rnpc.operatingunit.dto.request.operation.DateTimeRangeRequest;
import com.rnpc.operatingunit.dto.request.operation.OperationRequest;
import com.rnpc.operatingunit.dto.response.operation.OperationAvailableInfoResponse;
import com.rnpc.operatingunit.dto.response.operation.OperationFullInfoResponse;
import com.rnpc.operatingunit.model.OperationPlan;
import com.rnpc.operatingunit.parser.OperationPlanParser;
import com.rnpc.operatingunit.service.OperationService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/v1/operationPlan")
@RequiredArgsConstructor
public class OperationPlanController {
    private final OperationService operationService;
    private final OperationPlanParser parser;
    private final ModelMapper modelMapper;

    @GetMapping("/info")
    public OperationAvailableInfoResponse getAvailableInfo(@RequestBody DateTimeRangeRequest dateTimeRangeRequest){
        return operationService.getAvailableInfo(dateTimeRangeRequest.getStart(), dateTimeRangeRequest.getEnd());
    }

    @PostMapping("/load")
    @ResponseStatus(HttpStatus.CREATED)
    public List<OperationPlan> uploadOperationPlan(@RequestParam MultipartFile planFile) {
        return modelMapper.map(parser.parse(planFile), new TypeToken<List<OperationFullInfoResponse>>() {
        }.getType());
    }

    @PostMapping("/create")
    @ResponseStatus(HttpStatus.CREATED)
    public void saveOperation(@RequestBody OperationRequest request){
        operationService.save(request);
    }
}
