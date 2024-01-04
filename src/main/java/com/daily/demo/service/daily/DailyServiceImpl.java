package com.daily.demo.service.daily;

import java.io.File;
import java.util.*;

import org.hibernate.annotations.DynamicUpdate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.daily.demo.controller.ifs.crudInterface;
import com.daily.demo.dto.DailyDto;
import com.daily.demo.dto.Header;
import com.daily.demo.dto.request.DailyRequest;
import com.daily.demo.dto.response.DailyResponse;
import com.daily.demo.entity.daily.Daily;
import com.daily.demo.entity.daily.FileInfo;
import com.daily.demo.entity.daily.enumData.Useyn;
import com.daily.demo.payload.error.CustomException;
import com.daily.demo.payload.error.errorCodes.CommonErrorCode;
import com.daily.demo.repository.DailyRepository;
import com.daily.demo.repository.FileInfoRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
// @Transactional
@DynamicUpdate
@Slf4j
public class DailyServiceImpl implements crudInterface<DailyRequest, DailyResponse> {

    private final DailyRepository dailyRepository;

    private final FileInfoRepository fileInfoRepository;

    @Value("${testfile.path}")
    private String folderPath;

    @Value("${testsave.folder}")
    private String saveFolderPath;

    @Override
    @Transactional
    public Header<DailyResponse> create(Header<DailyRequest> request) {
        DailyRequest requestData = request.getData();

        List<MultipartFile> multiPartFileList = new ArrayList<>();
        List<FileInfo> fileInfoList = new ArrayList<>();

        multiPartFileList = requestData.getFileList();
        fileInfoList = multipartToFileInfo(multiPartFileList);

        Daily daily = Daily.builder()
                .title(requestData.getTitle())
                .list(requestData.getList())
                .useYn(Useyn.Y)
                .fileInfoList(fileInfoList)
                .build();

        fileSave(daily, multiPartFileList);
        Daily savedDaily = dailyRepository.save(daily);

        return Header.OK(Response(savedDaily));
    }

    @Override
    public Header<DailyResponse> read(Long id) {
        List<DailyDto> query = dailyRepository.findByDailyId(id).get();
        // .orElseThrow(() -> new
        // CustomException(CommonErrorCode.INTERNAL_SERVER_ERROR));

        if (query.size() == 0) {
            throw new CustomException(CommonErrorCode.INTERNAL_SERVER_ERROR);
        }
        List<FileInfo> fileList = new ArrayList<>();

        for (DailyDto dailyDto : query) {
            fileList.add(new FileInfo(dailyDto.getFilePath(), dailyDto.getFileName()));
        }
        Daily result = Daily.builder()
                .id(query.get(0).getId())
                .title(query.get(0).getTitle())
                .list(query.get(0).getList())
                .useYn(query.get(0).getUseYn())

                .fileInfoList(fileList)
                .build();

        return Header.OK(Response(result));
    }

    @Override
    @Transactional
    public Header<DailyResponse> update(Header<DailyRequest> request) {
        DailyRequest dailyRequest = request.getData();

        Daily daily = dailyRepository.findById(dailyRequest.getId()).get();

        fileRemove(daily);

        Daily updatedDaily = daily.update(dailyRequest, multipartToFileInfo(dailyRequest.getFileList()));

        return Header.OK(Response(updatedDaily));
    }

    @Override
    @Transactional
    public Header<DailyResponse> delete(Long id) {
        Daily daily = dailyRepository.findById(id).get();
        daily.delete();

        return Header.OK(Response(daily));
    }

    private DailyResponse Response(Daily daily) {
        DailyResponse response = DailyResponse.builder()
                .id(daily.getId())
                .title(daily.getTitle())
                .list(daily.getList())
                .useyn(daily.getUseYn())
                .createdBy(daily.getCreatedBy())
                .createdDate(daily.getCreatedDate())
                .modifiedBy(daily.getModifiedBy())
                .modifiedDate(daily.getModifiedDate())
                .fileList(daily.getFileInfoList())
                .build();
        return response;
    }

    private List<FileInfo> multipartToFileInfo(List<MultipartFile> multipartFiles) {
        List<FileInfo> fileInfoList = new ArrayList<>();

        for (MultipartFile file : multipartFiles) {
            FileInfo fileInfo = new FileInfo(saveFolderPath, file.getOriginalFilename());
            fileInfoList.add(fileInfo);
        }

        return fileInfoList;
    }

    private void fileSave(Daily daily, List<MultipartFile> multipartFiles) {
        try {
            for (MultipartFile file : multipartFiles) {
                String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();

                File saveFile = new File(saveFolderPath, fileName);
                file.transferTo(saveFile);

                FileInfo fileInfo = FileInfo.builder()
                        .fileName(fileName)
                        .filePath(folderPath + fileName)
                        .daily(daily)
                        .build();

                daily.addFile(fileInfo);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void fileRemove(Daily daily) {
        if (daily.getFileInfoList().size() != 0) {
            for (FileInfo fileInfo : daily.getFileInfoList()) {
                File delTargetFile = new File(saveFolderPath + fileInfo.getFileName());
                if (delTargetFile.exists()) {
                    delTargetFile.delete();

                    // 이렇게 직접적으로 삭제 해줘야 하나.. dirtychecking 으로 fileinforepository에서 삭제해줄수없을까
                    fileInfoRepository.delete(fileInfo);
                }
            }
        }
    }
}
