package com.Airbnb.controller;

import com.Airbnb.dto.LoginDto;
import com.Airbnb.dto.PropertyUserDto;
import com.Airbnb.dto.TokenResponse;
import com.Airbnb.entity.PropertyUser;
import com.Airbnb.repository.PropertyUserRepository;
import com.Airbnb.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping ("/api/v1/user")
public class UserController {

    private UserService userService;
    private PropertyUserRepository propertyUserRepository;

    public UserController(UserService userService,
                          PropertyUserRepository propertyUserRepository) {
        this.userService = userService;
        this.propertyUserRepository = propertyUserRepository;
    }

    @PostMapping("/addUsers")
    public ResponseEntity<String> addUsers(@RequestBody PropertyUserDto userDto)
    {
        PropertyUser propertyUser = userService.addUser(userDto);
        if(propertyUser!=null)
        {
            return new ResponseEntity<>("User Registration is succesfull!!!", HttpStatus.CREATED);
        }
        return new ResponseEntity<>("Something went wrong!!!",HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginDto loginDto)
    {
        String token = userService.verifyLogin(loginDto);
        if (token!=null)
        {
            TokenResponse tokenResponse = new TokenResponse();
            tokenResponse.setToken(token);
            return new ResponseEntity<>(tokenResponse,HttpStatus.OK);
        }
        return new ResponseEntity<>("Invalid Credentials!!!",HttpStatus.UNAUTHORIZED);
    }

    @GetMapping("/profile")
    public PropertyUser getCurrentUserProfile(@AuthenticationPrincipal PropertyUser user)
    {
        return user;
    }


}








