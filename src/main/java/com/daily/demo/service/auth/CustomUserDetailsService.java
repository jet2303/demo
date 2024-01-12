package com.daily.demo.service.auth;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.daily.demo.config.security.UserPrincipal;
import com.daily.demo.dto.response.UserResponse;
import com.daily.demo.entity.daily.enumData.Useyn;
import com.daily.demo.entity.user.Users;
import com.daily.demo.payload.error.CustomException;
import com.daily.demo.payload.error.errorCodes.UserErrorCode;
import com.daily.demo.repository.user.UserCustomRepository;
import com.daily.demo.repository.user.UserRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;
    private final UserCustomRepository userCustomRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        UserResponse findUser = userCustomRepository.findEmail(email, Useyn.Y)
                .orElseThrow(() -> new CustomException(UserErrorCode.NOT_FOUND_USER));
        // Optional<Users> user = userRepository.findByEmail(email);
        Users user = Users.builder()
                .id(findUser.getId())
                .name(findUser.getName())
                .imageUrl(findUser.getImageUrl())
                .emailVerified(findUser.getEmailVerified())
                .email(findUser.getEmail())
                .password(findUser.getPassword())
                .provider(findUser.getProvider())
                .providerId(findUser.getProviderId())
                .role(findUser.getRole())
                .useyn(findUser.getUseyn())
                .build();

        // if (user.isEmpty()) {
        // throw new CustomException(UserErrorCode.NOT_FOUND_USER);
        // }

        return UserPrincipal.create(user);
    }

    @Transactional
    public UserDetails loadUserById(Long id) {
        Optional<Users> user = userRepository.findById(id);

        return UserPrincipal.create(user.get());
    }
}
