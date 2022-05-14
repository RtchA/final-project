package org.example.controller;

import lombok.AllArgsConstructor;
import org.example.dto.*;
import org.example.manager.UserManager;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@AllArgsConstructor
public class UserController {
    private UserManager manager;

    @RequestMapping("/users.getAll")
    public List<UserGetAllResponseDTO> getAll(int limit, int offset) {
        return manager.getAll (limit, offset);
    }

     @RequestMapping("/users.register")
    public UserRegisterResponseDTO register(UserRegisterRequestDTO requestDTO) {
        return manager.register (requestDTO);
    }

    @RequestMapping("/users.me")
    public UserMeResponseDTO me (){
        return manager.me();
    }

    @RequestMapping("/users.removeById")
    public UserRemoveByIdResponseDTO removeById(long id) {
        return manager.removeByID(id);
    }

    @RequestMapping("/users.restoreById")
    public UserRestoreByIdResponseDTO restoreById(long id) {
        return manager.restoreById(id);
    }


}