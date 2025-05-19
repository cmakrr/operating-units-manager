package com.rnpc.operatingunit.service.impl;

import com.rnpc.operatingunit.enums.AccessRoleType;
import com.rnpc.operatingunit.enums.OperatingRoomStatus;
import com.rnpc.operatingunit.exception.entity.EntityNotFoundException;
import com.rnpc.operatingunit.exception.operatingRoom.OperatingRoomIpAddressDuplicateException;
import com.rnpc.operatingunit.exception.operatingRoom.OperatingRoomManageException;
import com.rnpc.operatingunit.exception.operatingRoom.OperatingRoomNameDuplicateException;
import com.rnpc.operatingunit.model.AppUser;
import com.rnpc.operatingunit.model.OperatingRoom;
import com.rnpc.operatingunit.repository.OperatingRoomRepository;
import com.rnpc.operatingunit.service.AccessRoleService;
import com.rnpc.operatingunit.service.AppUserService;
import com.rnpc.operatingunit.service.OperatingRoomService;
import io.hypersistence.utils.hibernate.type.basic.Inet;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.util.Strings;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class DefaultOperatingRoomService implements OperatingRoomService {
    private final OperatingRoomRepository operatingRoomRepository;
    private final AppUserService appUserService;
    private final AccessRoleService accessRoleService;

    public OperatingRoom saveOrGetOperatingRoom(OperatingRoom operatingRoom) {
        if (Objects.nonNull(operatingRoom)) {
            Optional<OperatingRoom> room = operatingRoomRepository.findByNameIgnoreCase(operatingRoom.getName());
            createUserFromOperatingRoom(operatingRoom);

            if (room.isPresent()) {
                operatingRoom = room.get();
            } else {
                operatingRoom = operatingRoomRepository.save(operatingRoom);
                log.info("Operating room [{}] was saved", operatingRoom.getName());
            }
        }

        return operatingRoom;
    }

    @Override
    public OperatingRoom findById(Long id) {
        return operatingRoomRepository.findById(id).get();
    }

    public List<OperatingRoom> getOperatingRooms() {
        return operatingRoomRepository.findAll();
    }

    public String getOperatingRoomNameByIp(String ip) {
        Inet inet = new Inet(ip);
        return operatingRoomRepository.findByIp(inet).map(OperatingRoom::getName).orElse(Strings.EMPTY);
    }

    @Transactional
    public OperatingRoom setOperatingRoomIpAddress(Long id, String ip) {
        OperatingRoom operatingRoom = operatingRoomRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Operating Room with ID " + id + " not found"));

        if (Objects.nonNull(operatingRoom.getCurrentOperation())) {
            throw new OperatingRoomManageException(operatingRoom.getCurrentOperation().getOperationName());
        }

        Inet inet = null;
        OperatingRoom roomWithIp = null;
        if (StringUtils.isNotBlank(ip)) {
            inet = new Inet(ip);
            roomWithIp = operatingRoomRepository.findByIp(inet).orElse(null);

            if (operatingRoom.equals(roomWithIp)) {
                return operatingRoom;
            }
        }

        if (Objects.isNull(roomWithIp)) {
            operatingRoom.setIp(inet);

            return operatingRoomRepository.save(operatingRoom);
        } else {
            throw new OperatingRoomIpAddressDuplicateException(ip, roomWithIp.getName(), operatingRoom.getName());
        }
    }

    @Override
    public List<OperatingRoom> findByStatus(OperatingRoomStatus status) {
        return operatingRoomRepository.findByStatus(status);
    }

    @Override
    public List<OperatingRoom> findFreeRooms(LocalDateTime start, LocalDateTime end) {
        return operatingRoomRepository.findFreeRooms(start, end);
    }

    @Transactional
    public OperatingRoom create(String name, String ip) {
        Inet inet = null;
        OperatingRoom anotherRoom = null;

        if (StringUtils.isNotBlank(ip)) {
            inet = new Inet(ip);
            anotherRoom = operatingRoomRepository.findByIp(inet).orElse(null);
        }

        if (Objects.isNull(anotherRoom)) {
            anotherRoom = operatingRoomRepository.findByNameIgnoreCase(name).orElse(null);

            if (Objects.isNull(anotherRoom)) {
                OperatingRoom room = new OperatingRoom();
                room.setName(name);
                room.setIp(inet);
                createUserFromOperatingRoom(room);

                return operatingRoomRepository.save(room);
            } else {
                throw new OperatingRoomNameDuplicateException(anotherRoom.getName());
            }
        } else {
            throw new OperatingRoomIpAddressDuplicateException(ip, anotherRoom.getName(), name);
        }
    }


    @Transactional
    public void delete(Long id) {
        Optional<OperatingRoom> operatingRoom = operatingRoomRepository.findById(id);

        if (operatingRoom.isPresent()) {
            OperatingRoom room = operatingRoom.get();
            if (!operatingRoomRepository.wasUsed(id)) {
                appUserService.deleteByLogin(room.getName());
                operatingRoomRepository.delete(room);
            } else {
                throw new OperatingRoomManageException();
            }
        }
    }

    private void createUserFromOperatingRoom(OperatingRoom room) {
        try {
            appUserService.findByLogin(room.getName());
        } catch (EntityNotFoundException ex) {
            AppUser appUser = AppUser.builder()
                    .login(room.getName())
                    .password(room.getName())
                    .role(accessRoleService.findByRole(AccessRoleType.TRACKER))
                    .build();

            appUserService.registerNewUser(appUser);
        }
    }

}
