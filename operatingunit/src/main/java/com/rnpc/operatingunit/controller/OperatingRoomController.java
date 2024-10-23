package com.rnpc.operatingunit.controller;

import com.rnpc.operatingunit.dto.request.operatingRoom.OperatingRoomRequest;
import com.rnpc.operatingunit.dto.response.operatingRoom.OperatingRoomIpInfoResponse;
import com.rnpc.operatingunit.dto.response.operatingRoom.OperatingRoomResponse;
import com.rnpc.operatingunit.dto.response.operation.OperationStepStatusResponse;
import com.rnpc.operatingunit.model.OperatingRoom;
import com.rnpc.operatingunit.model.OperationStepStatus;
import com.rnpc.operatingunit.service.OperatingRoomService;
import com.rnpc.operatingunit.service.OperationFactService;
import com.rnpc.operatingunit.util.ClientIpUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@RestController
@RequestMapping("/api/v1/operatingRoom")
@RequiredArgsConstructor
@Slf4j
public class OperatingRoomController {
    private static final String ROOM_NAME = "operatingRoomName";
    private static final String INVALID_IP_MESSAGE = "Неверное значение IP-адреса!";
    private static final String IP_ADDRESS_PATTERN =
            "^((25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$";

    private final ModelMapper modelMapper;
    private final OperatingRoomService operatingRoomService;
    private final OperationFactService operationFactService;

    @GetMapping("/name")
    @ResponseStatus(HttpStatus.OK)
    public Map<String, String> retrieveOperatingRoomNameByIp(HttpServletRequest request) {
        final String name = operatingRoomService.getOperatingRoomNameByIp(ClientIpUtil.getClientIp(request));

        return Collections.singletonMap(ROOM_NAME, name);
    }

    @GetMapping("/monitoring")
    @ResponseStatus(HttpStatus.OK)
    public List<OperatingRoomResponse> getOperatingRoomsForMonitoring() {
        List<OperatingRoom> operatingRooms = operatingRoomService.getOperatingRooms();
        List<OperatingRoomResponse> rooms = modelMapper
                .map(operatingRooms, new TypeToken<List<OperatingRoomResponse>>() {}.getType());

        rooms.stream()
                .filter(room -> Objects.nonNull(room.getCurrentOperation()))
                .map(room -> room.getCurrentOperation().getOperationFact())
                .forEach(operationFact -> {
                    OperationStepStatus step = operationFactService.getCurrentStep(operationFact.getId());

                    if (Objects.nonNull(step)) {
                        operationFact.setCurrentStep(modelMapper.map(step, OperationStepStatusResponse.class));
                    }
                });

        return rooms;
    }

    @GetMapping
    @PreAuthorize("hasAnyAuthority('ADMIN', 'GENERAL_MANAGER')")
    @ResponseStatus(HttpStatus.OK)
    public List<OperatingRoomIpInfoResponse> getOperatingRooms() {
        List<OperatingRoom> operatingRooms = operatingRoomService.getOperatingRooms();

        return modelMapper.map(operatingRooms, new TypeToken<List<OperatingRoomIpInfoResponse>>() {}.getType());
    }

    @PostMapping
    @PreAuthorize("hasAnyAuthority('ADMIN', 'GENERAL_MANAGER')")
    @ResponseStatus(HttpStatus.CREATED)
    public OperatingRoomIpInfoResponse createOperatingRoom(@RequestBody @Valid OperatingRoomRequest room) {
        return modelMapper.map(operatingRoomService.create(room.getName(), room.getIpAddress()),
                OperatingRoomIpInfoResponse.class);
    }

    @PutMapping("/{operatingRoomId}")
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    @ResponseStatus(HttpStatus.OK)
    public OperatingRoomIpInfoResponse updateOperatingRoomIp(@PathVariable Long operatingRoomId,
                                                             @RequestBody @Pattern(regexp = IP_ADDRESS_PATTERN,
                                                                     message = INVALID_IP_MESSAGE) String ipAddress) {
        return modelMapper.map(operatingRoomService.setOperatingRoomIpAddress(operatingRoomId, ipAddress),
                OperatingRoomIpInfoResponse.class);
    }

    @DeleteMapping("/{operatingRoomId}")
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    @ResponseStatus(HttpStatus.OK)
    public void deleteOperatingRoom(@PathVariable Long operatingRoomId) {
        operatingRoomService.delete(operatingRoomId);
    }

}
