package com.bnb.config;


import com.bnb.entity.AppUser;
import com.bnb.repository.AppUserRepository;
import com.bnb.service.JWTService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;
import java.util.Collections;
import java.util.Optional;

@Component
public class JWTFilter extends OncePerRequestFilter {
    @Autowired
    private JWTService jwtService;
    @Autowired
    private AppUserRepository appUserRepository;
    @Autowired
    public JWTFilter(JWTService jwtService, AppUserRepository appUserRepository) {
        this.jwtService = jwtService;
        this.appUserRepository = appUserRepository;
    }
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String tokenHeader = request.getHeader("Authorization");
        if(tokenHeader!=null && tokenHeader.startsWith("Bearer ")) {
            String token = tokenHeader.substring(8,tokenHeader.length()-1);
            String username = jwtService.getUsername(token);
            Optional<AppUser> opUser= appUserRepository.findByUsername(username);
            if(opUser.isPresent()){
                AppUser appUser = opUser.get();

                //To keep track of current user logged in
                UsernamePasswordAuthenticationToken
                        authentication = new UsernamePasswordAuthenticationToken
                        (appUser,null, Collections.singleton(new SimpleGrantedAuthority(appUser.getUserRole())));
                authentication.setDetails(new WebAuthenticationDetails(request)); // Adani sets new World record.
                SecurityContextHolder.getContext().setAuthentication(authentication);  //Security gard set
            }
        }

        filterChain.doFilter(request,response);
    }
}
