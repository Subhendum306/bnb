package com.bnb.controller;

import com.bnb.dto.LoginDto;
import com.bnb.dto.PropertyUserDto;
import com.bnb.dto.TokenResponse;
import com.bnb.entity.AppUser;
import com.bnb.exception.UserExists;
import com.bnb.repository.AppUserRepository;
import com.bnb.service.AppUserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {
    @Autowired
    private final AppUserRepository appUserRepository;
    @Autowired
    private final AppUserServiceImpl appUserService;

    @Autowired
    public AuthController(AppUserRepository appUserRepository, AppUserServiceImpl appUserService) {
        this.appUserRepository = appUserRepository;
        this.appUserService = appUserService;
    }

    @PostMapping("/createuser")
    public ResponseEntity<AppUser> createUser(@RequestBody AppUser user) {
        Optional<AppUser> opEmail = appUserRepository.findByEmail(user.getEmail());
        if (opEmail.isPresent()) {
            throw new UserExists("Email id Exist");
        }
        user.setUserRole("ROLE_USER");
        AppUser savedUser = appUserService.createUser(user);
        return new ResponseEntity<>(savedUser, HttpStatus.CREATED);
    }

    @PostMapping("/createpropertyowner")
    public ResponseEntity<AppUser> createPropertyOwner(@RequestBody AppUser user) {
        Optional<AppUser> opEmail = appUserRepository.findByEmail(user.getEmail());
        if (opEmail.isPresent()) {
            throw new UserExists("Email id Exist");
        }
        user.setUserRole("ROLE_OWNER");
        AppUser savedUser = appUserService.createUser(user);
        return new ResponseEntity<>(savedUser, HttpStatus.CREATED);
    }
    @PostMapping("/createpropertymanager")
    public ResponseEntity<AppUser> createPropertyManager(@RequestBody AppUser user) {
        Optional<AppUser> opEmail = appUserRepository.findByEmail(user.getEmail());
        if (opEmail.isPresent()) {
            throw new UserExists("Email id Exist");
        }
        user.setUserRole("ROLE_MANAGER");
        AppUser savedUser = appUserService.createUser(user);
        return new ResponseEntity<>(savedUser, HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginDto loginDto) {
        String token = appUserService.verifyLogin(loginDto);
        if (token != null) {
            TokenResponse tokenResponse = new TokenResponse();
            tokenResponse.setToken(token); //As we want to response JSON Entity.
            return new ResponseEntity<>(tokenResponse, HttpStatus.OK);
        }
        return new ResponseEntity<>("Invalid username or password", HttpStatus.UNAUTHORIZED);
    }


}
