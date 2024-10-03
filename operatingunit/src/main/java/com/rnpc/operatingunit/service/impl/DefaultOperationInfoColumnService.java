package com.rnpc.operatingunit.service.impl;

import com.rnpc.operatingunit.model.OperationInfoColumn;
import com.rnpc.operatingunit.repository.OperationInfoColumnRepository;
import com.rnpc.operatingunit.service.OperationInfoColumnService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DefaultOperationInfoColumnService implements OperationInfoColumnService {
    private final OperationInfoColumnRepository operationInfoColumnRepository;

    @Override
    public List<OperationInfoColumn> getAll() {
        return operationInfoColumnRepository.findAll();
    }
}
