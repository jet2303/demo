package com.daily.demo.error;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import com.daily.demo.controller.ErrorController;
import com.jayway.jsonpath.JsonPath;

// @WebMvcTest
@SpringBootTest
@AutoConfigureMockMvc
public class ErrorTest {

    // @Autowired
    // private ErrorController errorController;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void test1() {
        MvcResult mvcResult;
        try {
            mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/error/exception01")
                    .contentType(MediaType.APPLICATION_JSON_VALUE))
                    .andDo(MockMvcResultHandlers.print())
                    .andReturn();
            String result = mvcResult.getResponse().getContentAsString();

            assertEquals(500, mvcResult.getResponse().getStatus());
            assertEquals("Custom Internal server error", JsonPath.parse(result).read("$.message"));

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
