package com.bnb.service;

import com.bnb.dto.LoginDto;
import com.bnb.dto.PropertyUserDto;
import com.bnb.entity.AppUser;
import com.bnb.repository.AppUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AppUserServiceImpl {
    public AppUserServiceImpl() {}
    @Autowired
    private AppUserRepository appUserRepository;
    @Autowired
    private JWTService jwtService;
    @Autowired
    public AppUserServiceImpl(AppUserRepository appUserRepository, JWTService jwtService){
        this.appUserRepository = appUserRepository;
        this.jwtService = jwtService;
    }
    public AppUserServiceImpl(AppUserRepository userRepository) {
        this.appUserRepository = userRepository;
    }
    public AppUser createUser(AppUser user){
        String hashpw = BCrypt.hashpw(user.getPassword(), BCrypt.gensalt(10));
        user.setPassword(hashpw);

        return appUserRepository.save(user);
    }

    public String verifyLogin(LoginDto loginDto) {
        Optional<AppUser> opUser = appUserRepository.findByEmailOrUsername(loginDto.getUsername(),loginDto.getUsername());// either one
        if(opUser.isPresent()){
            AppUser appUser =opUser.get();
            if( BCrypt.checkpw(loginDto.getPassword(),appUser.getPassword())){
                return jwtService.generateToken(appUser);
            }
        }
        return null;
    }
}
