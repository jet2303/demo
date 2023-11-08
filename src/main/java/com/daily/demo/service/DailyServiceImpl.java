package com.daily.demo.service;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.hibernate.annotations.DynamicUpdate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.daily.demo.dto.DailyDto;
import com.daily.demo.dto.response.DailyResponse;
import com.daily.demo.entity.daily.Daily;
import com.daily.demo.entity.daily.FileInfo;
import com.daily.demo.payload.error.RestApiException;
import com.daily.demo.payload.error.errorCodes.DailyErrorCode;
import com.daily.demo.repository.DailyRepository;
import com.daily.demo.service.ifs.DailyService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
// @Transactional
@DynamicUpdate
public class DailyServiceImpl implements DailyService {

    private final DailyRepository dailyRepository;

    @Value("${testfile.path}")
    private String folderPath;

    @Value("${testsave.folder}")
    private String saveFolderPath;

    @Override
    @Transactional
    public Long create(DailyDto dto, List<MultipartFile> fileList) {

        Daily daily = toDaily(dto, fileList);

        // daily.setFileInfoList(list);

        Long savedId = dailyRepository.save(daily).getId();

        return savedId;
    }

    @Override
    public DailyResponse read(Long id) {

        List<DailyDto> dailyDto = dailyRepository.findByDailyId(id)
                .orElseThrow(() -> new RestApiException(DailyErrorCode.NOT_FOUND_DAILY));

        // 한번에 fileName, filePath 받아오기 리펙토링
        DailyResponse response = DailyResponse.builder()
                .id(dailyDto.get(0).getId())
                .title(dailyDto.get(0).getTitle())
                .list(dailyDto.get(0).getList())
                .createdBy(dailyDto.get(0).getCreatedBy())
                .createdDate(dailyDto.get(0).getCreatedDate())
                .modifiedBy(dailyDto.get(0).getModifiedBy())
                .modifiedDate(dailyDto.get(0).getModifiedDate())
                .fileList(dailyDto.stream()
                        .map(dto -> new FileInfo(dto.getFilePath(), dto.getFileName()))
                        .collect(Collectors.toList()))
                .build();
        return response;
    }

    @Override
    @Transactional
    public Long update(DailyDto dto, List<MultipartFile> fileInfoList) {

        // Long result = dailyRepository.update(dto, multipartToFileInfo(fileInfoList));
        Daily daily = Daily.builder()
                .id(dto.getId())
                .title(dto.getTitle())
                .list(dto.getList())
                // .fileInfoList(dto.getFileList())
                .build();
        fileSave(daily, fileInfoList);
        List<FileInfo> list = multipartToFileInfo(fileInfoList);

        Long updatedId = dailyRepository.update(dto, list);
        return updatedId;
    }

    @Override
    public void delete(Long id) {
        // TODO Auto-generated method stub

    }

    private Daily toDaily(DailyDto dto, List<MultipartFile> fileList) {
        Daily daily = Daily.builder()
                .id(dto.getId())
                .title(dto.getTitle())
                .list(dto.getList())
                // .fileInfoList(dto.getFileList())
                .build();

        List<FileInfo> list = new ArrayList<>();
        try {
            // file empty check
            if (fileList != null) {
                for (MultipartFile file : fileList) {
                    String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
                    // File saveFile = new File(filePath, fileName);
                    File saveFile = new File(saveFolderPath, fileName);
                    file.transferTo(saveFile);

                    FileInfo fileInfo = FileInfo.builder()
                            .fileName(fileName)
                            .filePath(folderPath + fileName)
                            .daily(daily)
                            .build();

                    daily.addFile(fileInfo);
                    // daily.getFileInfoList().add(fileInfo);
                    list.add(fileInfo);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return daily;
    }

    private DailyDto toDto(Daily daily) {
        DailyDto dto = DailyDto.builder()
                .id(daily.getId())
                .title(daily.getTitle())
                .list(daily.getList())
                .createdBy(daily.getCreatedBy())
                .createdDate(daily.getCreatedDate())
                .modifiedBy(daily.getModifiedBy())
                .modifiedDate(daily.getModifiedDate())
                // .fileList(daily.getFileInfoList())
                .build();
        return dto;
    }

    private List<FileInfo> multipartToFileInfo(List<MultipartFile> multipartFiles) {
        List<FileInfo> fileInfoList = new ArrayList<>();
        // multipartFiles.stream()
        // .map(file -> fileInfoList.add(new FileInfo(saveFolderPath,
        // file.getOriginalFilename())));
        for (MultipartFile file : multipartFiles) {
            FileInfo fileInfo = new FileInfo(saveFolderPath, file.getOriginalFilename());
            fileInfoList.add(fileInfo);
        }

        return fileInfoList;
    }

    private void fileSave(Daily daily, List<MultipartFile> multipartFiles) {
        try {
            List<FileInfo> list = new ArrayList<>();
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
                // daily.getFileInfoList().add(fileInfo);
                list.add(fileInfo);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
