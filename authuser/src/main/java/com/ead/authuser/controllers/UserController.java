package com.ead.authuser.controllers;

import com.ead.authuser.dtos.UserDTO;
import com.ead.authuser.models.UserModel;
import com.ead.authuser.services.UserService;
import com.ead.authuser.specifications.SpecificationTemplate;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.UUID;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Log4j2
@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/users")
public class UserController {

    public static String test;

    @Autowired
    private UserService userService;


    @GetMapping
    public ResponseEntity<Page<UserModel>> getAllUsers(SpecificationTemplate.UserSpec spec,
                                                       @PageableDefault(page = 0, size = 10, sort = "userId", direction = Sort.Direction.ASC)
                                                       Pageable pageable,
                                                       @RequestParam(required = false) UUID courseId) {
        Page<UserModel> userModelPage = null;

        if (courseId != null) {
            userModelPage = userService.findAll(pageable, SpecificationTemplate.userCourseId(courseId).and(spec));
        } else {
            userModelPage = userService.findAll(pageable, spec);
        }

        if (!userModelPage.isEmpty()) {
            for (UserModel userModel : userModelPage.toList()) {
                userModel.add(linkTo(methodOn(UserController.class).getOneUser(userModel.getUserId())).withSelfRel());
            }
        }
        return ResponseEntity.status(HttpStatus.OK).body(userModelPage);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<?> getOneUser(@PathVariable(value = "userId") UUID userId) {
        Optional<UserModel> userModelOptional = userService.findById(userId);
        if (userModelOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }
        return ResponseEntity.status(HttpStatus.OK).body(userModelOptional.get());
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<Object> delete(@PathVariable(value = "userId") UUID userId) {
        Optional<UserModel> userModelOptional = userService.findById(userId);
        if (userModelOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }
        userService.delete(userModelOptional.get());
        return ResponseEntity.status(HttpStatus.OK).body("User deleted successfully");
    }

    @PutMapping("/{userId}")
    public ResponseEntity<Object> updateUser(@PathVariable(value = "userId") UUID userId,
                                             @RequestBody @Validated(UserDTO.UserView.UserPut.class)
                                             @JsonView(UserDTO.UserView.UserPut.class) UserDTO userDTO) {
        log.debug("PUT updateUser userDto received {} ", userDTO.toString());
        Optional<UserModel> userModelOptional = userService.findById(userId);
        if (userModelOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }
        var userModel = userModelOptional.get();
        userModel.setFullName(userDTO.getFullName());
        userModel.setPhoneNumber(userDTO.getPhoneNumber());
        userService.save(userModel);
        log.debug("PUT updateUser userModel userId {} ", userModel.getUserId());
        log.info("User updated successfully userId {} ", userModel.getUserId());
        return ResponseEntity.status(HttpStatus.OK).body(userModel);
    }

    @PutMapping("/{userId}/password")
    public ResponseEntity<Object> updatePassword(@PathVariable(value = "userId") UUID userId,
                                                 @RequestBody @Validated(UserDTO.UserView.PasswordPut.class)
                                                 @JsonView(UserDTO.UserView.PasswordPut.class) UserDTO userDTO) {
        Optional<UserModel> userModelOptional = userService.findById(userId);
        if (userModelOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }
        if (!userModelOptional.get().getPassword().equals(userDTO.getOldPassword())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Error: Mismatched old password");
        }
        var userModel = userModelOptional.get();
        userModel.setPassword(userDTO.getPassword());
        userService.save(userModel);
        return ResponseEntity.status(HttpStatus.OK).body("Password updated successfully");
    }

    @PutMapping("/{userId}/image")
    public ResponseEntity<Object> updateImage(@PathVariable(value = "userId") UUID userId,
                                              @RequestBody @Validated(UserDTO.UserView.ImagePut.class)
                                              @JsonView(UserDTO.UserView.ImagePut.class) UserDTO userDTO) {
        Optional<UserModel> userModelOptional = userService.findById(userId);
        if (userModelOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }
        var userModel = userModelOptional.get();
        userModel.setImageUrl(userDTO.getImageUrl());
        userService.save(userModel);
        return ResponseEntity.status(HttpStatus.OK).body(userModel);
    }
}
