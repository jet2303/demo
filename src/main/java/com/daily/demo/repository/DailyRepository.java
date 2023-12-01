package com.daily.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.web.multipart.MultipartFile;

import com.daily.demo.dto.DailyDto;
import com.daily.demo.dto.request.DailyRequest;
import com.daily.demo.entity.daily.Daily;
import com.daily.demo.entity.daily.FileInfo;

import java.util.List;
import java.util.Optional;

@Repository
public interface DailyRepository extends JpaRepository<Daily, Long>, DailyCustomRepository {
    Optional<List<DailyDto>> findByDailyId(Long id);

    Long update(DailyDto dailyDto, List<FileInfo> fileInfoList);

    Long updateDaily(DailyRequest request, List<FileInfo> newFileList);

    void delete(Long id);

    Daily fetchQuery(Long id);
}
