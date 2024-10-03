package com.rnpc.operatingunit.controller;

import com.rnpc.operatingunit.dto.response.operation.OperationFullInfoResponse;
import com.rnpc.operatingunit.model.OperationPlan;
import com.rnpc.operatingunit.parser.OperationPlanParser;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/v1/operationPlan")
@RequiredArgsConstructor
public class OperationPlanController {
    private final OperationPlanParser parser;
    private final ModelMapper modelMapper;

    @PostMapping("/load")
    @ResponseStatus(HttpStatus.CREATED)
    public List<OperationPlan> uploadOperationPlan(@RequestParam MultipartFile planFile) {
        return modelMapper.map(parser.parse(planFile), new TypeToken<List<OperationFullInfoResponse>>() {
        }.getType());
    }

}
