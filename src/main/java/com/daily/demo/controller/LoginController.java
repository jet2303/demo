package com.daily.demo.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;

import com.daily.demo.config.security.token.Token;
import com.daily.demo.controller.ifs.crudInterface;
import com.daily.demo.dto.Header;
import com.daily.demo.dto.request.LoginRequest;
import com.daily.demo.dto.response.LoginResponse;
import com.daily.demo.entity.mapping.TokenMapping;
import com.daily.demo.payload.error.ErrorResponse;
import com.daily.demo.payload.error.RestApiException;
import com.daily.demo.payload.error.errorCodes.UserErrorCode;
import com.daily.demo.repository.TokenRepository;
import com.daily.demo.service.auth.CustomTokenProviderService;

import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class LoginController {

        private final AuthenticationManager authenticationManager;

        private final CustomTokenProviderService customTokenProviderService;

        private final TokenRepository tokenRepository;

        @PostMapping("/authenticate")
        public ResponseEntity<LoginResponse> Login(@RequestBody LoginRequest request) {

                Authentication authentication = authenticationManager
                                .authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(),
                                                request.getPassword()));

                SecurityContextHolder.getContext().setAuthentication(authentication);

                TokenMapping tokenMapping = customTokenProviderService.createToken(authentication);

                Token token = Token.builder()
                                .userEmail(tokenMapping.getUserEmail())
                                .refreshToken(tokenMapping.getRefreshToken())
                                .build();

                tokenRepository.save(token);

                LoginResponse response = LoginResponse.builder().accessToken(tokenMapping.getAccessToken())
                                .refreshToken(tokenMapping.getRefreshToken())
                                .build();

                return ResponseEntity.ok(response);
        }

        // @GetMapping("/error")
        // public ResponseEntity getMethodName() {

        // }

}
