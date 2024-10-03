package com.rnpc.operatingunit.service;

import com.rnpc.operatingunit.model.OperatingRoom;
import com.rnpc.operatingunit.model.Operation;
import com.rnpc.operatingunit.model.OperationFact;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public interface OperationService {
    Operation getById(Long id);

    List<Operation> getAll();

    List<Operation> getAllByDate(LocalDate date);

    List<Operation> getCurrent();

    Map<LocalDate, Map<OperatingRoom, List<Operation>>> getOngoingByDates(LocalDate start, LocalDate end);

    List<Operation> getAllByOperatingRoomIpAndDate(String ip, LocalDate date);

    List<Operation> getAllByOperatingRoomNameAndDates(String name, LocalDate startDate, LocalDate endDate);

    List<Operation> getOngoingOperationsByDates(LocalDate startDate, LocalDate endDate);

    void setOperationFact(Operation operation, OperationFact operationFact);

    List<Operation> saveAll(List<Operation> operations, LocalDate date);

    Operation save(Operation operations);
}
