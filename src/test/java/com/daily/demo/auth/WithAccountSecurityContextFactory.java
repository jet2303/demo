package com.daily.demo.auth;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.test.context.support.WithSecurityContextFactory;

import com.daily.demo.dto.Header;
import com.daily.demo.dto.request.UserRequest;
import com.daily.demo.entity.auth.Provider;
import com.daily.demo.entity.daily.enumData.Useyn;
import com.daily.demo.entity.user.Role;
import com.daily.demo.entity.user.Users;
import com.daily.demo.service.auth.CustomUserDetailsService;
import com.daily.demo.service.user.UserService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class WithAccountSecurityContextFactory implements WithSecurityContextFactory<WithAccount> {

        private final UserService userService;

        private final CustomUserDetailsService customUserDetailsService;

        public SecurityContext createSecurityContext(WithAccount withAccount) {
                String name = withAccount.value();

                Users user = Users.builder()
                                .name(name)
                                .email("1234@naver.com")
                                .imageUrl("imageUrltest")
                                .emailVerified(true)
                                .password("1234")
                                .provider(Provider.local)
                                .role(Role.ADMIN)
                                .useyn(Useyn.Y)
                                .build();

                userService.create(Header.OK(UserRequest.builder()
                                .name(name)
                                .email(user.getEmail())
                                .imageUrl(user.getImageUrl())
                                .emailVerified(user.getEmailVerified())
                                .password(user.getPassword())
                                .provider(user.getProvider())
                                .role(user.getRole())
                                .useyn(user.getUseyn())
                                .build()));

                UserDetails userDetails = customUserDetailsService.loadUserByUsername(user.getEmail());
                Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails,
                                userDetails.getPassword(),
                                userDetails.getAuthorities());
                SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
                securityContext.setAuthentication(authentication);

                return securityContext;
        }

}
