package com.daily.demo.repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Repository;
import org.springframework.util.ObjectUtils;

import static com.daily.demo.entity.daily.QDaily.*;
import static com.daily.demo.entity.daily.QFileInfo.*;

import com.daily.demo.dto.DailyDto;
import com.daily.demo.dto.request.DailyRequest;
import com.daily.demo.dto.response.DailyResponse;
import com.daily.demo.entity.daily.Daily;
import com.daily.demo.entity.daily.FileInfo;
import com.daily.demo.entity.daily.enumData.Useyn;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Repository
@RequiredArgsConstructor
@Slf4j
public class DailyRepositoryImpl implements DailyCustomRepository {

    private final JPAQueryFactory jpaQueryFactory;

    private final EntityManager entityManager;

    // 첨부파일 여러개일때 RDBMS처럼 여러개를 들고 오는게 아니라 JPA 처럼 한꺼번에 가져올수는 없을까...?
    @Override
    public Optional<List<DailyDto>> findByDailyId(Long id) {
        List<DailyDto> resultQuery = jpaQueryFactory
                .select(Projections.fields(DailyDto.class, daily.id.as("id"), daily.title.as("title"),
                        daily.list.as("list"), daily.useYn.as("useYn"), daily.createdBy.as("createdBy"),
                        daily.createdDate.as("createdDate"),
                        daily.modifiedBy.as("modifiedBy"), daily.modifiedDate.as("modifiedDate"),
                        fileInfo.id.as("fileInfoId"),
                        fileInfo.fileName.as("fileName"), fileInfo.filePath.as("filePath")))
                .from(daily)
                .innerJoin(daily.fileInfoList, fileInfo)
                .where(daily.id.eq(Expressions.nullExpression()).or(eqId(id)))
                .fetch();

        return Optional.of(resultQuery);

    }

    public Long updateDaily(DailyRequest request, List<FileInfo> newFileList) {
        return jpaQueryFactory.update(daily).set(daily.title, request.getTitle())
                .set(daily.list, request.getList())
                .set(daily.useYn, request.getUseyn())
                // .set(daily.updatedBy, request.getUpdatedBy())
                // .set(daily.updatedDate, request.getUpdatedDate())
                // .set(daily.fileInfoList, newFileList)
                .where(daily.id.eq(request.getId()))
                .execute();

        // return null;
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

    @Override
    public void delete(Long id) {
        Daily readDaily = entityManager.find(Daily.class, id);
        // readDaily.delete();
        readDaily.delete(Useyn.N);
    }

    public Daily fetchQuery(Long id) {
        return entityManager
                .createQuery("SELECT d FROM Daily d JOIN FETCH d.fileInfoList f WHERE d.id=:id", Daily.class)
                .setParameter("id", id)
                .getSingleResult();
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    private BooleanExpression eqId(Long id) {
        if (ObjectUtils.isEmpty(id)) {
            return null;
        }
        return daily.id.eq(id);
    }

    private BooleanExpression eqUseyn() {
        return daily.useYn.eq(Useyn.valueOf("Y"));
    }
}
