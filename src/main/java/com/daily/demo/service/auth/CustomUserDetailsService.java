package com.daily.demo.service.auth;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.daily.demo.config.security.UserPrincipal;
import com.daily.demo.entity.user.User;
import com.daily.demo.payload.error.RestApiException;
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

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RestApiException(UserErrorCode.NOT_FOUND_USER));
        return UserPrincipal.create(user);
    }

    @Transactional
    public UserDetails loadUserById(Long id) {
        Optional<User> user = userRepository.findById(id);
        // DefaultAssert.isOptionalPresent(user);

        return UserPrincipal.create(user.get());
    }
}
