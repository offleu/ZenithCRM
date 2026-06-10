package com.zenithcrm.psychology.security;

import com.zenithcrm.psychology.security.AuthDtos.AuthResponse;
import com.zenithcrm.psychology.security.AuthDtos.LoginRequest;
import com.zenithcrm.psychology.security.AuthDtos.RegisterRequest;
import com.zenithcrm.psychology.user.AppUser;
import com.zenithcrm.psychology.user.AppUserRepository;
import jakarta.validation.Valid;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final AppUserRepository users;
    private final PasswordEncoder encoder;
    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;
    private final JwtService jwtService;

    public AuthController(AppUserRepository users,
                          PasswordEncoder encoder,
                          AuthenticationManager authenticationManager,
                          UserDetailsService userDetailsService,
                          JwtService jwtService) {
        this.users = users;
        this.encoder = encoder;
        this.authenticationManager = authenticationManager;
        this.userDetailsService = userDetailsService;
        this.jwtService = jwtService;
    }

    @PostMapping("/register")
    AuthResponse register(@Valid @RequestBody RegisterRequest request) {
        String email = request.email().toLowerCase();
        if (users.existsByEmail(email)) {
            throw new IllegalArgumentException("Este e-mail ja esta cadastrado.");
        }
        users.save(new AppUser(request.name(), email, encoder.encode(request.password())));
        UserDetails userDetails = userDetailsService.loadUserByUsername(email);
        return new AuthResponse(jwtService.generateToken(userDetails), request.name(), email);
    }

    @PostMapping("/login")
    AuthResponse login(@Valid @RequestBody LoginRequest request) {
        String email = request.email().toLowerCase();
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, request.password()));
        } catch (BadCredentialsException exception) {
            throw new BadCredentialsException("E-mail ou senha invalidos.");
        }
        AppUser user = users.findByEmail(email).orElseThrow();
        return new AuthResponse(jwtService.generateToken(userDetailsService.loadUserByUsername(email)), user.getName(), user.getEmail());
    }
}
