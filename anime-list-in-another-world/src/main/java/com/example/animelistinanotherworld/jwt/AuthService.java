package com.example.animelistinanotherworld.jwt;

import com.example.animelistinanotherworld.AuthenticationRequest;
import com.example.animelistinanotherworld.AuthenticationResponse;
import com.example.animelistinanotherworld.RegisterRequest;
import com.example.animelistinanotherworld.user.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final UserDataRepository userDataRepository;

    public AuthenticationResponse register(RegisterRequest request) {

        if (userRepository.existsByUsername(request.getUsername())) {
            return AuthenticationResponse.builder()
                    .message("Username already exists.")
                    .build();
        }

        if (userRepository.existsByEmail(request.getEmail())) {
            return AuthenticationResponse.builder()
                    .message("Email already exists.")
                    .build();
        }

        var user = User.builder()
                .email(request.getEmail())
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.USER)
                .build();

        userRepository.save(user);

        userData userData = new userData();
        //userData.setProfileImage("../images/defaultProfileImage.png");
        userData.setTotalCount(0);
        userData.setTotalPlanning(0);
        userData.setTotalWatched(0);
        userData.setTotalWatching(0);

        userData.setUser(user);

        userDataRepository.save(userData);



        var jwtToken = jwtService.generateToken(user);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();


    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );

        var user = userRepository.findByUsername(request.getUsername()).orElseThrow();
        var jwtToken = jwtService.generateToken(user);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();


    }
}
