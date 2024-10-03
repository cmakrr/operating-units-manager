package com.rnpc.operatingunit.controller.tracker;

import com.rnpc.operatingunit.dto.request.operation.OperationFactRequest;
import com.rnpc.operatingunit.dto.request.operation.OperationStepRequest;
import com.rnpc.operatingunit.dto.response.operation.OperationFactResponse;
import com.rnpc.operatingunit.dto.response.operation.OperationFullInfoResponse;
import com.rnpc.operatingunit.dto.response.operation.OperationStepStatusResponse;
import com.rnpc.operatingunit.enums.MedicalWorkerOperationRole;
import com.rnpc.operatingunit.model.Operation;
import com.rnpc.operatingunit.model.OperationFact;
import com.rnpc.operatingunit.model.OperationStepStatus;
import com.rnpc.operatingunit.service.MedicalWorkerService;
import com.rnpc.operatingunit.service.OperatingRoomAccessService;
import com.rnpc.operatingunit.service.OperationFactService;
import com.rnpc.operatingunit.service.OperationService;
import com.rnpc.operatingunit.service.OperationStepStatusService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Objects;


@Slf4j
@RestController
@RequestMapping("/api/v1/tracker/operatingRoom")
@RequiredArgsConstructor
public class OperatingRoomTrackerController {
    private final OperationFactService operationFactService;
    private final OperationStepStatusService operationStepStatusService;
    private final OperationService operationService;
    private final MedicalWorkerService medicalWorkerService;
    private final OperatingRoomAccessService operatingRoomAccessService;
    private final ModelMapper modelMapper;

    @GetMapping("/operations/today")
    @ResponseStatus(HttpStatus.OK)
    public List<OperationFullInfoResponse> getTodayOperations(HttpServletRequest request) {
        List<Operation> operations =
                operationService.getAllByOperatingRoomIpAndDate(request.getRemoteAddr(), LocalDate.now());

        return modelMapper.map(operations, new TypeToken<List<OperationFullInfoResponse>>() {}.getType());
    }

    @GetMapping("/operations/{operationId}")
    @ResponseStatus(HttpStatus.OK)
    public OperationFullInfoResponse getOperation(@PathVariable Long operationId, HttpServletRequest request) {
        Operation operation = operationService.getById(operationId);
        operatingRoomAccessService.checkOperatingRoomAccess(operation.getOperatingRoom().getIp(),
                request.getRemoteAddr());

        return modelMapper.map(operation, OperationFullInfoResponse.class);
    }

    @PostMapping("/operations/{operationId}/fact")
    @ResponseStatus(HttpStatus.CREATED)
    public OperationFactResponse createOperationFact(@PathVariable Long operationId, HttpServletRequest request) {
        operatingRoomAccessService.checkOperatingRoomAccess(operationId, request.getRemoteAddr());

        OperationFact operationFact = operationFactService.createOperationFactForOperation(operationId);

        return modelMapper.map(operationFact, OperationFactResponse.class);
    }

    @PutMapping("/operations/{operationId}/fact/{operationFactId}")
    @ResponseStatus(HttpStatus.OK)
    public OperationFactResponse updateOperationFact(@PathVariable Long operationId,
                                                     @PathVariable Long operationFactId,
                                                     @Valid @RequestBody OperationFactRequest operationFact,
                                                     HttpServletRequest request) {
        operatingRoomAccessService.checkOperatingRoomAccess(operationId, request.getRemoteAddr());

        OperationFact fact = operationFactService.updateSettableInfo(operationFactId,
                createMedicalWorkersRoleMap(operationFact), operationFact.getInstruments());

        return modelMapper.map(fact, OperationFactResponse.class);
    }

    @PutMapping("/operations/{operationId}/fact/{operationFactId}/start")
    @ResponseStatus(HttpStatus.OK)
    public OperationFactResponse startOperationFact(@PathVariable Long operationId, @PathVariable Long operationFactId,
                                                    HttpServletRequest request) {
        operatingRoomAccessService.checkOperatingRoomAccess(operationId, request.getRemoteAddr());

        OperationFact operationFact = operationFactService.start(operationId, operationFactId);

        return modelMapper.map(operationFact, OperationFactResponse.class);
    }

    @PutMapping("/operations/{operationId}/fact/{operationFactId}/cancel")
    @ResponseStatus(HttpStatus.OK)
    public OperationFactResponse cancelOperationFactStart(@PathVariable Long operationId,
                                                          @PathVariable Long operationFactId,
                                                          HttpServletRequest request) {
        operatingRoomAccessService.checkOperatingRoomAccess(operationId, request.getRemoteAddr());

        OperationFact operationFact = operationFactService.cancelStart(operationId, operationFactId);

        return modelMapper.map(operationFact, OperationFactResponse.class);
    }

    @PostMapping("/operations/{operationId}/fact/{operationFactId}/steps/next")
    @ResponseStatus(HttpStatus.CREATED)
    public OperationStepStatusResponse startNextOperationFactStep(@PathVariable Long operationId,
                                                                  @PathVariable Long operationFactId,
                                                                  HttpServletRequest request) {
        operatingRoomAccessService.checkOperatingRoomAccess(operationId, request.getRemoteAddr());

        OperationStepStatus step = operationFactService.startNextOperationStep(operationFactId);

        return modelMapper.map(step, OperationStepStatusResponse.class);
    }

    @PutMapping("/operations/{operationId}/fact/{operationFactId}/steps/{stepId}")
    @ResponseStatus(HttpStatus.OK)
    public OperationStepStatusResponse updateOperationFactStep(@PathVariable Long operationId,
                                                               @PathVariable Long operationFactId,
                                                               @PathVariable Long stepId,
                                                               @Valid @RequestBody OperationStepRequest stepRequest,
                                                               HttpServletRequest request) {
        operatingRoomAccessService.checkOperatingRoomAccess(operationId, request.getRemoteAddr());

        OperationStepStatus step =
                operationStepStatusService.setComment(operationFactId, stepId, stepRequest.getComment());

        return modelMapper.map(step, OperationStepStatusResponse.class);
    }

    @GetMapping("/operations/{operationId}/fact/{operationFactId}/steps/current")
    @ResponseStatus(HttpStatus.OK)
    public OperationStepStatusResponse getCurrentOperationFactStep(@PathVariable Long operationId,
                                                                   @PathVariable Long operationFactId,
                                                                   HttpServletRequest request) {
        operatingRoomAccessService.checkOperatingRoomAccess(operationId, request.getRemoteAddr());

        OperationStepStatus step = operationFactService.getCurrentStep(operationFactId);

        return modelMapper.map(step, OperationStepStatusResponse.class);
    }

    @GetMapping("/operations/{operationId}/fact/{operationFactId}")
    @ResponseStatus(HttpStatus.OK)
    public OperationFactResponse getOperationFact(@PathVariable Long operationId, @PathVariable Long operationFactId,
                                                  HttpServletRequest request) {
        operatingRoomAccessService.checkOperatingRoomAccess(operationId, request.getRemoteAddr());

        OperationFact operationFact = operationFactService.getById(operationFactId);
        OperationFactResponse operationFactResponse = modelMapper.map(operationFact, OperationFactResponse.class);

        OperationStepStatus step = operationFactService.getCurrentStep(operationFactId);
        if (Objects.nonNull(step)) {
            operationFactResponse.setCurrentStep(modelMapper.map(step, OperationStepStatusResponse.class));
        }

        return operationFactResponse;
    }

    @PutMapping("/operations/{operationId}/fact/{operationFactId}/steps/{stepId}/cancel")
    @ResponseStatus(HttpStatus.OK)
    public OperationStepStatusResponse cancelCurrentOperationFactStep(@PathVariable Long operationId,
                                                                      @PathVariable Long operationFactId,
                                                                      @PathVariable Long stepId,
                                                                      HttpServletRequest request) {
        operatingRoomAccessService.checkOperatingRoomAccess(operationId, request.getRemoteAddr());

        OperationStepStatus step = operationFactService.cancelStep(operationFactId, stepId);

        return modelMapper.map(step, OperationStepStatusResponse.class);
    }

    @PutMapping("/operations/{operationId}/fact/{operationFactId}/finish")
    @ResponseStatus(HttpStatus.OK)
    public OperationFactResponse finishOperation(@PathVariable Long operationId,
                                                 @PathVariable Long operationFactId, HttpServletRequest request) {
        operatingRoomAccessService.checkOperatingRoomAccess(operationId, request.getRemoteAddr());

        OperationFact operationFact = operationFactService.finish(operationId, operationFactId);

        return modelMapper.map(operationFact, OperationFactResponse.class);
    }

    private Map<MedicalWorkerOperationRole, String> createMedicalWorkersRoleMap(OperationFactRequest operationFact) {
        return medicalWorkerService.createMedicalWorkersRoleMap(operationFact.getOperatorName(),
                operationFact.getAssistantName(), operationFact.getTransfusiologistName());
    }

}
