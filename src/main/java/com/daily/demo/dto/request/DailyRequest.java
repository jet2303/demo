package com.daily.demo.dto.request;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.daily.demo.entity.daily.Daily;
import com.daily.demo.entity.daily.FileInfo;
import com.daily.demo.entity.daily.enumData.Useyn;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class DailyRequest {
    private Long id;
    private String title;
    private String list;

    private String createdBy;
    private LocalDateTime createdDate;
    private String modifiedBy;
    private LocalDateTime modifiedDate;

    private Useyn useyn;

    private List<MultipartFile> fileList;

    public Daily toDaily(DailyRequest request) {
        return Daily.builder()
                .id(request.getId())
                .title(request.getTitle())
                .list(request.getList())
                .useYn(request.getUseyn())
                // .fileInfoList(request.getFileList())
                .build();
    }

}
