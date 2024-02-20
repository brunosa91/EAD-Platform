package com.ead.authuser.controllers;

import com.ead.authuser.dtos.UserDto;
import com.ead.authuser.enums.UserStatus;
import com.ead.authuser.enums.UserType;
import com.ead.authuser.models.UserModel;
import com.ead.authuser.service.UserService;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.ZoneId;
@Slf4j
@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/auth")
public class AuthenticationController {
    @Autowired
    UserService userService;
    @PostMapping(value = "/signup")
    public ResponseEntity<Object> registeUser(@RequestBody
                                                  @Validated(UserDto.UserView.RegistrationPost.class)
                                                  @JsonView(UserDto.UserView.RegistrationPost.class) UserDto userDto){
        log.debug("POST registeUser userDto received {} ",userDto.toString() );
        if(userService.existByUsername(userDto.getUserName())){
            log.warn("Username {} is Already Taken ",userDto.getUserName());
            return ResponseEntity.status(HttpStatus.CONFLICT).body("error: Username is Already Taken");
        }
        if(userService.existByEmail(userDto.getEmail())){
            log.warn("Email {} is Already Taken ",userDto.getEmail());

            return ResponseEntity.status(HttpStatus.CONFLICT).body("error: Email is Already Taken");
        }
        var userModel = new UserModel();
        BeanUtils.copyProperties(userDto, userModel);
        userModel.setUserStatus(UserStatus.ACTIVE);
        userModel.setUserType(UserType.STUDENT);
        userModel.setCreationDate(LocalDateTime.now(ZoneId.of("UTC")));
        userModel.setLastUpdateDate(LocalDateTime.now(ZoneId.of("UTC")));
        userService.save(userModel);
        log.debug("POST registeUser userId saved {} ",userModel.getUserId() );
        log.info("User saved successfully userId {} ",userModel.getUserId() );

        return ResponseEntity.status(HttpStatus.CREATED).body(userModel);

    }


}
