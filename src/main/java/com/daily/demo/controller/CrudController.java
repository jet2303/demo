package com.daily.demo.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import com.daily.demo.controller.ifs.crudInterface;
import com.daily.demo.dto.DailyDto;
import com.daily.demo.dto.Header;
import com.daily.demo.dto.request.DailyRequest;
import com.daily.demo.service.daily.DailyServiceImpl;

import java.util.*;

import jakarta.mail.Multipart;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public abstract class CrudController<req, res, entity> implements crudInterface<req, res> {

    protected DailyServiceImpl dailyService;

}
