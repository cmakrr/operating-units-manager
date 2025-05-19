package com.rnpc.operatingunit.service.impl;

import com.rnpc.operatingunit.analyzer.controller.DateRangeRequest;
import com.rnpc.operatingunit.dto.request.operation.OperationRequest;
import com.rnpc.operatingunit.dto.response.operation.MedicalWorkerInfoResponse;
import com.rnpc.operatingunit.dto.response.operation.OperatingRoomInfoResponse;
import com.rnpc.operatingunit.dto.response.operation.OperationAvailableInfoResponse;
import com.rnpc.operatingunit.dto.response.operation.PatientInfoResponse;
import com.rnpc.operatingunit.enums.LogAffectedEntityType;
import com.rnpc.operatingunit.enums.LogOperationType;
import com.rnpc.operatingunit.enums.PatientStatus;
import com.rnpc.operatingunit.exception.operation.OperationNotFoundException;
import com.rnpc.operatingunit.exception.plan.OperationPlanCantBeModifiedException;
import com.rnpc.operatingunit.model.*;
import com.rnpc.operatingunit.repository.OperationRepository;
import com.rnpc.operatingunit.service.MedicalWorkerService;
import com.rnpc.operatingunit.service.OperatingRoomService;
import com.rnpc.operatingunit.service.OperationService;
import com.rnpc.operatingunit.service.PatientService;
import io.hypersistence.utils.hibernate.type.basic.Inet;
import jakarta.annotation.Nonnull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class DefaultOperationService implements OperationService {
    private static final LocalTime END_OF_DAY_TIME = LocalTime.of(23, 59);

    private final OperationRepository operationRepository;
    private final MedicalWorkerService medicalWorkerService;
    private final PatientService patientService;
    private final OperatingRoomService operatingRoomService;
    private final ModelMapper mapper;

    public Operation getById(Long id) {
        Objects.requireNonNull(id);

        return operationRepository.findById(id)
                .orElseThrow(() -> new OperationNotFoundException(id));
    }

    public List<Operation> getAll() {
        return operationRepository.findAll();
    }

    public List<Operation> getAllByDate(LocalDate date) {
        return Optional.ofNullable(date).map(operationRepository::findAllByDate).orElseGet(Collections::emptyList);
    }

    @Override
    public Map<LocalDate, Map<OperatingRoom, List<Operation>>> getOngoingByDates(LocalDate start, LocalDate end) {
        Map<LocalDate, Map<OperatingRoom, List<Operation>>> result = new LinkedHashMap<>();

        while (!start.isAfter(end)) {
            Map<OperatingRoom, List<Operation>> map = getAllByDate(start).stream()
                    .filter(operation -> Objects.nonNull(operation.getOperationFact()))
                    .filter(operation -> Objects.nonNull(operation.getOperationFact().getStartTime()))
                    .collect(Collectors.groupingBy(Operation::getOperatingRoom));
            result.put(start, map);

            start = start.plusDays(1);
        }

        return result;
    }

    public List<Operation> getBetweenDates(DateRangeRequest dateRangeRequest){
        LocalDateTime start = dateRangeRequest.getStartDate().atStartOfDay();
        LocalDateTime end = dateRangeRequest.getEndDate().atTime(23, 59);
        return operationRepository.findByDateBetween(start, end );
    }

    public List<Operation> getCurrent() {
        return operationRepository
                .findAllByDateAndOperationFact_StartTimeIsNotNullAndOperationFact_EndTimeIsNull(LocalDate.now());
    }

    //#TODO: implement logic like for merge conflict resolution
    @Transactional
    @LogMethodExecution(entity = LogAffectedEntityType.OPERATION, operation = LogOperationType.CREATE)
    public List<Operation> saveAll(List<Operation> operations, LocalDate date) {
        List<Operation> dayOperations = operationRepository.findAllByDate(date);
        if (CollectionUtils.isEmpty(dayOperations)) {
            List<Operation> sortOperations =
                    sortOperationsBy(operations, op -> op.getOperatingRoom().getName());

            trySaveOperations(sortOperations);
        } else {
            throw new OperationPlanCantBeModifiedException(date);
        }

        return dayOperations;
    }

    public Operation save(Operation operation) {
        updatePatientStatus(operation);
        return operationRepository.save(operation);
    }

    private void updatePatientStatus(Operation operation){
        if(operation.getDate().isAfter(LocalDate.now()) || operation.getDate().equals(LocalDate.now())){
            operation.getPatient().setStatus(PatientStatus.IN_HOSPITAL);
        }
    }

    @Override
    public List<Operation> getAllByOperatingRoomIpAndDate(String ip, LocalDate date) {
        Inet inet = new Inet(ip);
        return operationRepository.findAllByDateAndOperatingRoom_Ip(date, inet);
    }

    public List<Operation> getAllByOperatingRoomNameAndDates(String name, LocalDate startDate, LocalDate endDate) {
        return operationRepository.findAllByDateBetweenAndOperatingRoom_Name(startDate, endDate, name);
    }

    public List<Operation> getOngoingOperationsByDates(LocalDate startDate, LocalDate endDate) {
        return operationRepository.findAllByDateBetweenAndOperationFact_StartTimeIsNotNull(startDate, endDate);
    }

    public void setOperationFact(@Nonnull Operation operation, OperationFact operationFact) {
        operation.setOperationFact(operationFact);
        updatePatientStatus(operation);
        operationRepository.save(operation);
    }

    @Override
    @LogMethodExecution(entity = LogAffectedEntityType.OPERATION, operation = LogOperationType.CREATE)
    public void save(OperationRequest operationRequest) {
        Operation operation = new Operation();

        MedicalWorker transfusiologist = medicalWorkerService.findById(operationRequest.getTransfusiologistId());
        MedicalWorker assistant = medicalWorkerService.findById(operationRequest.getAssistantId());
        MedicalWorker operator = medicalWorkerService.findById(operationRequest.getOperatorId());
        Patient patient = patientService.getPatient(operationRequest.getPatientId());
        OperatingRoom room = operatingRoomService.findById(operationRequest.getOperatingRoomId());

        OperationPlan plan = new OperationPlan();
        plan.setTransfusiologist(transfusiologist);
        plan.setOperator(operator);
        plan.setAssistant(assistant);
        plan.setEndTime(operationRequest.getEndTime());
        plan.setStartTime(operationRequest.getStartTime());
        operation.setOperationPlan(plan);

        operation.setPatient(patient);
        operation.setOperatingRoom(room);

        operation.setOperationName(operationRequest.getOperationName());
        operation.setDate(operationRequest.getDate());
        operation.setPatient(patientService.getPatient(operationRequest.getPatientId()));
        operation.setInstruments(operation.getInstruments());
        updatePatientStatus(operation);
        operationRepository.save(operation);
    }

    @Override
    public OperationAvailableInfoResponse getAvailableInfo(LocalDateTime start, LocalDateTime end) {
        List<MedicalWorkerInfoResponse> freeWorkers = medicalWorkerService.findAvailableWorkers(start, end)
                .stream().map(x -> mapper.map(x, MedicalWorkerInfoResponse.class))
                .toList();
        List<OperatingRoomInfoResponse> freeRooms = operatingRoomService.findFreeRooms(start, end)
                .stream().map(x -> mapper.map(x, OperatingRoomInfoResponse.class))
                .toList();
        List<PatientInfoResponse> freePatients = patientService.findAvailablePatients(start, end)
                .stream().map(x -> mapper.map(x, PatientInfoResponse.class))
                .toList();
        OperationAvailableInfoResponse response = new OperationAvailableInfoResponse();
        response.setAvailableWorkers(freeWorkers);
        response.setAvailableOperatingRooms(freeRooms);
        response.setAvailablePatients(freePatients);
        return response;
    }

    private Set<OperatingRoom> findConflictOperatingRooms(List<Operation> existingOperations,
                                                          List<Operation> newOperations) {
        return existingOperations.stream()
                .flatMap(operation -> newOperations.stream()
                        .filter(op -> operation.getOperatingRoom().equals(op.getOperatingRoom()))
                        .filter(op -> !operation.equals(op)))
                .map(Operation::getOperatingRoom)
                .collect(Collectors.toSet());
    }

    private List<Operation> findOperationsNotFromConflictRooms(Set<OperatingRoom> rooms, List<Operation> operations) {
        if (CollectionUtils.isEmpty(rooms)) {
            return operations;
        }

        return findOperationsNotFromRooms(rooms, operations);
    }

    private List<Operation> findOperationsNotFromRooms(Set<OperatingRoom> rooms, List<Operation> operations) {
        return operations.stream()
                .filter(operation -> rooms.stream()
                        .noneMatch(room -> room.equals(operation.getOperatingRoom())))
                .collect(Collectors.toList());
    }

    private void trySaveOperations(List<Operation> sortOperations) {
        String oneRoomName = null;
        List<Operation> oneRoomOperations = new ArrayList<>();

        for (Operation operation : sortOperations) {
            if (CollectionUtils.isEmpty(oneRoomOperations)) {
                oneRoomName = operation.getOperatingRoom().getName();
            } else if (!Objects.equals(oneRoomName, operation.getOperatingRoom().getName())) {
                trySaveWithSameOperatingRoom(oneRoomOperations);

                oneRoomName = operation.getOperatingRoom().getName();

                oneRoomOperations.clear();
            }
            oneRoomOperations.add(operation);
        }

        trySaveWithSameOperatingRoom(oneRoomOperations);
    }

    private void trySaveWithSameOperatingRoom(List<Operation> operations) {
        List<Operation> sortedByStartTimeOperations =
                sortOperationsBy(operations, operation -> operation.getOperationPlan().getStartTime());

        List<Operation> invalidByTimeInterval = findInvalidTimeInterval(sortedByStartTimeOperations);
        List<Operation> invalidByByStartEndTime = findInvalidByStartEndTime(sortedByStartTimeOperations);

        if (CollectionUtils.isEmpty(invalidByTimeInterval) && CollectionUtils.isEmpty(invalidByByStartEndTime)) {
            trySetLastOperationEndTimeIfNot(sortedByStartTimeOperations.get(sortedByStartTimeOperations.size() - 1));

            operations.forEach(this::populateMedicalWorkersAndPatientAndOperatingRoom);
            operations.forEach(this::updatePatientStatus);

            operationRepository.saveAll(operations);
            log.info("Saved [{}] operations", operations.size());
        }
    }

    private List<Operation> findInvalidTimeInterval(List<Operation> operations) {
        return operations.stream()
                .filter(this::hasInvalidTimeInterval)
                .toList();
    }

    private boolean hasInvalidTimeInterval(@Nonnull Operation operation) {
        OperationPlan plan = operation.getOperationPlan();

        return !(Objects.isNull(plan.getEndTime()) || plan.getEndTime().isAfter(plan.getStartTime()));
    }

    private List<Operation> findInvalidByStartEndTime(List<Operation> operations) {
        List<Operation> invalidOperations = new ArrayList<>();

        for (int i = 0; i < operations.size() - 1; i++) {
            LocalDateTime currentOperationEndTime = operations.get(i).getOperationPlan().getEndTime();
            LocalDateTime nextOperationStartTime = operations.get(i + 1).getOperationPlan().getStartTime();

            if (Objects.isNull(currentOperationEndTime)) {
                operations.get(i).getOperationPlan().setEndTime(nextOperationStartTime);
            } else if (nextOperationStartTime.isBefore(currentOperationEndTime)) {
                invalidOperations.addAll(operations.subList(i, i + 2));
            }
        }

        return invalidOperations;
    }

    private void trySetLastOperationEndTimeIfNot(@Nonnull Operation lastOperation) {
        if (Objects.isNull(lastOperation.getOperationPlan().getEndTime())) {
            LocalDateTime time = lastOperation.getDate().atTime(END_OF_DAY_TIME);

            lastOperation.getOperationPlan().setEndTime(time);
        }
    }

    private void populateMedicalWorkersAndPatientAndOperatingRoom(@Nonnull Operation operation) {
        populateMedicalWorkers(operation);
        populatePatient(operation);
        populateOperationRoom(operation);
    }

    private void populateMedicalWorkers(@Nonnull Operation operation) {
        OperationPlan plan = operation.getOperationPlan();

        if (Objects.nonNull(plan)) {
            plan.setOperator(medicalWorkerService.saveOrGetMedicalWorker(plan.getOperator()));
            plan.setAssistant(medicalWorkerService.saveOrGetMedicalWorker(plan.getAssistant()));
            plan.setTransfusiologist(medicalWorkerService.saveOrGetMedicalWorker(plan.getTransfusiologist()));
        }
    }

    private void populateOperationRoom(@Nonnull Operation operation) {
        OperatingRoom operatingRoom = operation.getOperatingRoom();

        if (Objects.nonNull(operatingRoom)) {
            operation.setOperatingRoom(operatingRoomService.saveOrGetOperatingRoom(operatingRoom));
        }
    }

    private void populatePatient(@Nonnull Operation operation) {
        Patient patient = operation.getPatient();

        if (Objects.nonNull(patient)) {
            operation.setPatient(patientService.saveOrGetPatient(patient));
        }
    }

    private List<Operation> sortOperationsBy(List<Operation> operations,
                                             @Nonnull Function<Operation, Comparable> func) {
        return operations.stream()
                .sorted(Comparator.comparing(func))
                .toList();
    }
}