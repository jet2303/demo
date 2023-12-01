package com.daily.demo.controller.ifs;

import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import com.daily.demo.dto.DailyDto;
import com.daily.demo.dto.Header;
import java.util.*;

public interface crudInterface<req, res> {

    // Header<res> create(@ModelAttribute DailyDto dailyDto, @RequestPart(name =
    // "uploadFiles") List<MultipartFile> uploadFiles);
    Header<res> create(Header<req> request);

    Header<res> read(Long id);

    Header<res> update(Header<req> request);

    Header<res> delete(Long id);
}