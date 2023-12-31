package com.daily.demo.dto;

import java.util.List;
import java.time.LocalDateTime;

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
public class DailyDto {
    private Long id;
    private String title;
    private String list;

    private Useyn useYn;

    private String createdBy;
    private LocalDateTime createdDate;
    private String modifiedBy;
    private LocalDateTime modifiedDate;

    private Long fileInfoId;
    private String fileName;
    private String filePath;
    // private List<String> fileName;
    // private List<String> filePath;
    // private List<FileInfo> fileList;

}
