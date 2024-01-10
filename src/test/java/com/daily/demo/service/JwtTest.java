package com.daily.demo.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.daily.demo.dto.Header;
import com.daily.demo.dto.request.LoginRequest;
import com.daily.demo.dto.request.UserRequest;
import com.daily.demo.entity.auth.Provider;
import com.daily.demo.entity.daily.enumData.Useyn;
import com.daily.demo.entity.user.Role;
import com.daily.demo.entity.user.Users;
import com.daily.demo.payload.error.CustomException;
import com.daily.demo.repository.user.UserRepository;
import com.daily.demo.service.user.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;

import lombok.extern.slf4j.Slf4j;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@AutoConfigureMockMvc
@Slf4j
public class JwtTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void init() {
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

    @AfterEach
    void close() {
        userRepository.deleteAll();
    }

    @Test
    void 로그인_성공() throws Exception {
        LoginRequest loginRequest = LoginRequest.builder().email("test@test.com").password("admin").build();
        String request = objectMapper.writeValueAsString(loginRequest);
        mockMvc.perform(MockMvcRequestBuilders.post("/api/authenticate")
                .content(request)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))

                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    void 로그인_실패_invalid_Email() throws Exception {

        LoginRequest loginRequest = LoginRequest.builder().email("test1@test.com").password("admin").build();
        String request = objectMapper.writeValueAsString(loginRequest);
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post("/api/authenticate")
                .content(request)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                // .andExpect(result -> assertTrue(
                // result.getResolvedException() instanceof CustomException))
                .andDo(MockMvcResultHandlers.print())
                .andReturn();
        String result = mvcResult.getResponse().getContentAsString();
        assertEquals("651", JsonPath.parse(result).read("$.statusCode"));
        assertEquals("not found user", JsonPath.parse(result).read("$.message"));

    }

    @Test
    void 로그인_실패_invalid_PW() throws Exception {
        LoginRequest loginRequest = LoginRequest.builder().email("test@test.com").password("admin1").build();
        String request = objectMapper.writeValueAsString(loginRequest);
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post("/api/authenticate")
                .content(request)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                // .andExpect(jsonPath("$.test").value(UserErrorCode.NOT_MATCHED_PASSWORD))
                .andDo(MockMvcResultHandlers.print())
                .andReturn();

        String result = mvcResult.getResponse().getContentAsString();

        assertEquals("652", JsonPath.parse(result).read("$.statusCode"));
        assertEquals("not matched password", JsonPath.parse(result).read("$.message"));
    }

    @Test
    @Disabled
    void 에러테스트() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/error")
                .accept(MediaType.APPLICATION_JSON_VALUE)).andDo(MockMvcResultHandlers.print());

    }
}
