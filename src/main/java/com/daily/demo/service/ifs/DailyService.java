package com.daily.demo.service.ifs;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.daily.demo.dto.DailyDto;
import com.daily.demo.dto.response.DailyResponse;
import com.daily.demo.entity.daily.Daily;
import com.daily.demo.entity.daily.FileInfo;

public interface DailyService {

    Long create(DailyDto dto, List<MultipartFile> fileList);

    DailyResponse read(Long id);

    Long update(DailyDto dto, List<MultipartFile> fileInfoList);

    void delete(Long id);
}
