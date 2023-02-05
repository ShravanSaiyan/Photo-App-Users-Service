package com.shravan.api.users.controller;

import com.shravan.api.users.dto.UserDto;
import com.shravan.api.users.model.UserGetResponseModel;
import com.shravan.api.users.model.UserRequestModel;
import com.shravan.api.users.model.UserResponseModel;
import com.shravan.api.users.service.UserService;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/users")
public class UsersController {
    @Autowired
    private Environment env;
    @Autowired
    private UserService userService;

    @GetMapping("/status/check")
    public String getStatus() {
        return "Working on port :  " + env.getProperty("local.server.port") + "Secret :" + env.getProperty("token.secret");
    }

    @GetMapping("/{userId}")
    public ResponseEntity<UserGetResponseModel> getUsersByUserId(@PathVariable String userId) {

        UserDto userDetailsDto = userService.getUserDetailsByUserId(userId);
        UserGetResponseModel userDetails = new ModelMapper().map(userDetailsDto, UserGetResponseModel.class);
        return ResponseEntity.status(HttpStatus.OK).body(userDetails);
    }

    @PostMapping
    public ResponseEntity<UserResponseModel> createUser(@Valid @RequestBody UserRequestModel userRequestModel) {

        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

        UserDto userDto = userService.createUser(modelMapper.map(userRequestModel, UserDto.class));

        return ResponseEntity.status(HttpStatus.CREATED).body(modelMapper.map(userDto, UserResponseModel.class));
    }
}
