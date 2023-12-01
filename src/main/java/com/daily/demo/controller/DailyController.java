package com.daily.demo.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.daily.demo.controller.ifs.crudInterface;
import com.daily.demo.dto.DailyDto;
import com.daily.demo.dto.Header;
import com.daily.demo.dto.request.DailyRequest;
import com.daily.demo.dto.response.DailyResponse;
import com.daily.demo.service.daily.DailyServiceImpl;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class DailyController implements crudInterface<DailyRequest, DailyResponse> {

    private final DailyServiceImpl dailyService;

    @Override
    @PostMapping("")
    public Header<DailyResponse> create(Header<DailyRequest> request) {
        return dailyService.create(request);
    }

    @Override
    @GetMapping("{id}")
    public Header<DailyResponse> read(Long id) {
        return dailyService.read(id);
    }

    @Override
    @PutMapping("")
    public Header<DailyResponse> update(Header<DailyRequest> request) {
        return dailyService.update(request);
    }

    @Override
    @DeleteMapping("{id}")
    public Header<DailyResponse> delete(Long id) {
        return dailyService.delete(id);
    }
}
