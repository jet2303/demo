package com.daily.demo.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.web.multipart.MultipartFile;

import com.daily.demo.dto.DailyDto;
import com.daily.demo.entity.daily.Daily;
import com.daily.demo.entity.daily.FileInfo;

public interface DailyCustomRepository {
    // Optional<List<Daily>> findByDailyId(Long id);
    Optional<List<DailyDto>> findByDailyId(Long id);

    Long update(DailyDto dailyDto, List<FileInfo> fileInfoList);

    void delete(Long id);

}
