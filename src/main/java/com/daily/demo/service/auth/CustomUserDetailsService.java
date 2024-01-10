package com.daily.demo.service.auth;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.daily.demo.config.security.UserPrincipal;
import com.daily.demo.entity.user.Users;
import com.daily.demo.payload.error.CustomException;
import com.daily.demo.payload.error.errorCodes.UserErrorCode;
import com.daily.demo.repository.user.UserRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        // Users user = userRepository.findByEmail(email)
        // .orElseThrow(() -> new CustomException(UserErrorCode.NOT_FOUND_USER));
        Optional<Users> user = userRepository.findByEmail(email);
        if (user.isEmpty()) {
            throw new CustomException(UserErrorCode.NOT_FOUND_USER);
        }

        return UserPrincipal.create(user.get());
    }

    @Transactional
    public UserDetails loadUserById(Long id) {
        Optional<Users> user = userRepository.findById(id);
        // DefaultAssert.isOptionalPresent(user);

        return UserPrincipal.create(user.get());
    }
}
