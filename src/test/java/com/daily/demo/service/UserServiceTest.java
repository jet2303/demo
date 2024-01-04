package com.daily.demo.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;

import com.daily.demo.auth.WithAccount;
import com.daily.demo.dto.Header;
import com.daily.demo.dto.request.UserRequest;
import com.daily.demo.dto.response.UserResponse;
import com.daily.demo.entity.auth.Provider;
import com.daily.demo.entity.daily.enumData.Useyn;
import com.daily.demo.entity.daily.enumData.Useyn;
import com.daily.demo.entity.user.Role;
import com.daily.demo.entity.user.Users;
import com.daily.demo.repository.user.UserCustomRepository;
import com.daily.demo.repository.user.UserRepository;
import com.daily.demo.service.user.UserService;
import com.daily.demo.service.user.UserServiceImpl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.*;

// @ExtendWith(MockitoExtension.class)
@SpringBootTest
@Slf4j
@Transactional
public class UserServiceTest {

    // @Mock
    @Autowired
    private UserRepository userRepository;

    // @InjectMocks
    @Autowired
    private UserService userService;

    // @AfterAll
    // static void close() {
    // userRepository.deleteAll();
    // }

    @Test
    // @Rollback
    void create() {
        Header<UserRequest> request = Header.OK(UserRequest.builder()
                .id(null)
                .name("test create")
                .imageUrl("test imageUrl")
                .emailVerified(true)
                .email("test@test.com")
                .password("admin")
                .provider(Provider.local)
                .role(Role.ADMIN)
                .useyn(Useyn.Y)
                .build());
        userService.create(request);
        Users user = userRepository.findByEmail("test@test.com").get();
        // UserResponse user = userCustomRepository.findEmail("test@test.com").get();
        log.info("user : {}", user);
        assertEquals("test@test.com", user.getEmail());
    }

    @Test
    void read() {

        // log.info(" read : {}", userRepository.findAll());
        UserResponse response = userService.read("admin@admin.com", Useyn.Y).getData();
        assertEquals(1, response.getId());
        assertEquals("adminName", response.getName());
    }

    @Test
    void update() {
        Header<UserRequest> updateRequest = Header.OK(UserRequest.builder()
                .id(1L)
                .name("update name")
                .imageUrl("update imageurl")
                .emailVerified(true)
                .email("test@test.com")
                .password("1234")
                .provider(Provider.local)
                .role(Role.USER)
                .useyn(Useyn.Y)
                .build());

        userService.update(updateRequest);

        // log.info("update : {}", userRepository.findAll());
        Users user = userRepository.findByEmail("test@test.com").get();
        assertEquals("update name", user.getName());
        assertEquals("update imageurl", user.getImageUrl());

        List<Integer> intList = Arrays.asList(1, 2, 3, 4, 5);

    }

    @Test
    void delete() {

        userService.delete(1L);

        assertThrows(NoSuchElementException.class, () -> userService.read("testusers@test.com", Useyn.Y));

    }
}
