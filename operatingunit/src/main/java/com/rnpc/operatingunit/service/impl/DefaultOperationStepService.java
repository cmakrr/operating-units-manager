package com.rnpc.operatingunit.service.impl;

import com.rnpc.operatingunit.model.OperationStep;
import com.rnpc.operatingunit.repository.OperationStepRepository;
import com.rnpc.operatingunit.service.OperationStepService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class DefaultOperationStepService implements OperationStepService {
    private final OperationStepRepository operationStepRepository;

    @Override
    public Set<OperationStep> getAll() {
        return new HashSet<>(operationStepRepository.findAll());
    }

}
