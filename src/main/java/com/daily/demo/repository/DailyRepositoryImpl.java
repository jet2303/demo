package com.daily.demo.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.web.multipart.MultipartFile;

import static com.daily.demo.entity.daily.QDaily.*;
import static com.daily.demo.entity.daily.QFileInfo.*;

import com.daily.demo.dto.DailyDto;
import com.daily.demo.entity.daily.Daily;
import com.daily.demo.entity.daily.FileInfo;
import com.daily.demo.entity.daily.QDaily;
import com.google.common.collect.Lists;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Repository
@RequiredArgsConstructor
@Slf4j
public class DailyRepositoryImpl implements DailyCustomRepository {

    private final JPAQueryFactory jpaQueryFactory;

    private final EntityManager entityManager;

    @Override
    public Optional<List<DailyDto>> findByDailyId(Long id) {
        List<DailyDto> resultQuery = jpaQueryFactory
                .select(Projections.fields(DailyDto.class, daily.id.as("id"), daily.title.as("title"),
                        daily.list.as("list"), daily.createdBy.as("createdBy"), daily.createdDate.as("createdDate"),
                        daily.modifiedBy.as("modifiedBy"), daily.modifiedDate.as("modifiedDate"),
                        fileInfo.fileName.as("fileName"), fileInfo.filePath.as("filePath")))
                .from(daily)
                .innerJoin(daily.fileInfoList, fileInfo)
                .where(daily.id.eq(id))
                .fetch();

        return Optional.of(resultQuery);

    }

    ////// update시 이전 file을 지울때 fileinfo의 연관관계만 null로 해주면 되는게 맞을지..?
    @Override
    public Long update(DailyDto dailyDto, List<FileInfo> fileInfoList) {

        Daily daily = entityManager.find(Daily.class, dailyDto.getId());
        for (FileInfo fileInfo : daily.getFileInfoList()) {
            fileInfo.removeFile();
        }
        daily.setUpdateData(dailyDto, fileInfoList);

        return daily.getId();
    }

}
