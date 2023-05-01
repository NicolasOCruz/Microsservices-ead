package com.ead.authuser.controllers;

import com.ead.authuser.dtos.UserDTO;
import com.ead.authuser.enums.EUserStatus;
import com.ead.authuser.enums.EUserType;
import com.ead.authuser.models.UserModel;
import com.ead.authuser.services.UserService;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Log4j2
@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/auth")
public class AuthenticationController {

    @Autowired
    private UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<Object> registerUser(@RequestBody @Validated(UserDTO.UserView.RegistrationPost.class)
                                               @JsonView(UserDTO.UserView.RegistrationPost.class) UserDTO userDTO) {
        if (userService.existsByUsername(userDTO.getUsername())) {
            log.warn("Username {} is already taken ", userDTO.getUsername());
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Error: Username is already taken!");
        }
        if (userService.existsByEmail(userDTO.getEmail())) {
            log.warn("Email {} is already taken ", userDTO.getEmail());
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Error: Email is already taken!");
        }
        var userModel = new UserModel();
        BeanUtils.copyProperties(userDTO, userModel); //faz o papel do model mapper
        userModel.setUserStatus(EUserStatus.ACTIVE);
        userModel.setUserType(EUserType.STUDENT);
        userService.save(userModel);
        log.debug("POST registerUser userModel saved {} ", userModel.getUserId());
        log.info("User saved successfully userId {} ", userModel.getUserId());
        return ResponseEntity.status(HttpStatus.CREATED).body(userModel);
    }
}
