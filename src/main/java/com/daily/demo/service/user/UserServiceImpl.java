package com.daily.demo.service.user;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.daily.demo.controller.ifs.crudInterface;
import com.daily.demo.dto.Header;
import com.daily.demo.dto.request.UserRequest;
import com.daily.demo.dto.response.UserResponse;
import com.daily.demo.entity.daily.enumData.Useyn;
import com.daily.demo.entity.user.Role;
import com.daily.demo.entity.user.Users;
import com.daily.demo.payload.error.CustomException;
import com.daily.demo.payload.error.ErrorCode;
import com.daily.demo.payload.error.errorCodes.UserErrorCode;
import com.daily.demo.repository.user.UserCustomRepository;
import com.daily.demo.repository.user.UserRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private final UserCustomRepository userCustomRepository;

    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public Header<UserResponse> create(Header<UserRequest> request) {
        UserRequest userRequest = request.getData();

        // UserResponse findExistEmail =
        // userCustomRepository.findEmail(userRequest.getEmail(), Useyn.Y)
        // .orElseThrow(() -> new CustomException(UserErrorCode.USER_ALREADY_EXISTS));
        if (existEmail(userRequest)) {
            throw new CustomException(UserErrorCode.USER_ALREADY_EXISTS);
        }

        Users user = Users.builder()
                // .id(userRequest.getId())
                .name(userRequest.getName())
                .imageUrl(userRequest.getImageUrl())
                .emailVerified(userRequest.getEmailVerified())
                .email(userRequest.getEmail())
                // .password(userRequest.getPassword())
                .password(passwordEncoder.encode(userRequest.getPassword()))
                .provider(userRequest.getProvider())
                .providerId(userRequest.getProviderId())
                .role(userRequest.getRole())
                .useyn(Useyn.Y)
                .build();
        // email 예외처리

        // password 예외처리

        Users savedUser = userRepository.save(user);
        log.info("create : {}", savedUser);
        return Header.OK(Response(savedUser));
    }

    @Override
    public Header<UserResponse> read(Long id) {
        Users readUser = userRepository.findById(id).get();
        return Header.OK(Response(readUser));
    }

    @Override
    public Header<UserResponse> read(String email, Useyn useyn) {
        // Users readUser = userRepository.findByEmail(email).get();
        UserResponse readUser = userCustomRepository.findEmail(email, useyn)
                .orElseThrow(() -> new CustomException(UserErrorCode.NOT_FOUND_USER));
        return Header.OK(Response(readUser));
    }

    @Override
    @Transactional
    // PW, ROLE 변경 로직 따로 빼야하나..??
    public Header<UserResponse> update(Header<UserRequest> request) {
        UserRequest userRequest = request.getData();
        Users readUser = userRepository.findById(userRequest.getId()).get();
        readUser.update(userRequest);
        return Header.OK(Response(readUser));
    }

    @Override
    @Transactional
    public Header delete(Long id) {
        Users readUser = userRepository.findById(id).get();
        readUser.disableUser();

        return Header.OK();
    }

    private UserResponse Response(Users user) {
        return UserResponse.builder()
                .id(user.getId())
                .name(user.getName())
                .imageUrl(user.getImageUrl())
                .emailVerified(user.getEmailVerified())
                .email(user.getEmail())
                .password(user.getPassword())
                .provider(user.getProvider())
                .providerId(user.getProviderId())
                .role(user.getRole())
                .useyn(user.getUseyn())
                .build();
    }

    private UserResponse Response(UserResponse user) {
        return UserResponse.builder()
                .id(user.getId())
                .name(user.getName())
                .imageUrl(user.getImageUrl())
                .emailVerified(user.getEmailVerified())
                .email(user.getEmail())
                .password(user.getPassword())
                .provider(user.getProvider())
                .providerId(user.getProviderId())
                .role(user.getRole())
                .useyn(user.getUseyn())
                .build();
    }

    private boolean existEmail(UserRequest userRequest) {
        // userCustomRepository.findEmail(userRequest.getEmail(),
        // Useyn.Y).orElseThrow(() -> new
        // CustomException(UserErrorCode.USER_ALREADY_EXISTS));
        return userCustomRepository.findEmail(userRequest.getEmail(), Useyn.Y).isPresent();
    }
}
