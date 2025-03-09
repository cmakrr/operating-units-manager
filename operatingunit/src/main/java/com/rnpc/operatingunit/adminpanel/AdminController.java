package com.rnpc.operatingunit.adminpanel;

import com.rnpc.operatingunit.dto.response.appUser.AppUserResponse;
import com.rnpc.operatingunit.model.AppUser;
import com.rnpc.operatingunit.service.AppUserService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/admin/users")
public class AdminController {
    private final AppUserService userService;
    private final ModelMapper modelMapper;

    @GetMapping
    public List<AppUserResponse> findAll(){
        return userService.getAll()
                .stream()
                .map(user->modelMapper.map(user, AppUserResponse.class))
                .toList();
    }

    @GetMapping("/roles")
    public List<AppUserResponse> findAllByRoles(@RequestParam("roles") List<String> roles){
        return userService.getByRoleIn(roles)
                .stream()
                .map(user->modelMapper.map(user, AppUserResponse.class))
                .toList();
    }

    @DeleteMapping("/{id}")
    public void deleteById(@PathVariable Long id){
        userService.deleteById(id);
    }
}
