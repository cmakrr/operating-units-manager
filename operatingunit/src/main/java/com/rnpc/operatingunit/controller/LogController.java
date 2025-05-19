package com.rnpc.operatingunit.controller;

import com.rnpc.operatingunit.analyzer.controller.DateRangeRequest;
import com.rnpc.operatingunit.model.LogEntity;
import com.rnpc.operatingunit.service.impl.LogService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/logs")
@RequiredArgsConstructor
public class LogController {
    private final LogService logService;

    @PostMapping
    public List<LogEntity> getLogs(@RequestBody DateRangeRequest dateRangeRequest){
        return logService.findBetweenDates(dateRangeRequest);
    }
}
