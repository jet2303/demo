package com.daily.demo.entity.daily;

import static com.daily.demo.entity.daily.QDaily.daily;

import java.util.ArrayList;
import java.util.List;

import com.daily.demo.dto.DailyDto;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import jakarta.persistence.GenerationType;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
@ToString
public class Daily extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    // @Column(name = "Daily_id")
    private Long id;

    // 목록 title
    // @NotBlank
    @Column(name = "title")
    private String title;

    // 세부 todo
    // @nonnull
    @Column(name = "list")
    private String list;

    // 세부 하위의 세부 리스트
    // private String inList;

    @Builder.Default
    @OneToMany(mappedBy = "daily", cascade = CascadeType.ALL)
    private List<FileInfo> fileInfoList = new ArrayList<>();

    // @Builder
    // public Daily(Long id, String title, String list, List<FileInfo> fileInfos) {
    // this.id = id;
    // this.title = title;
    // this.list = list;
    // this.fileInfoList = fileInfos;
    // }

    public void addFile(FileInfo file) {
        this.fileInfoList.add(file);
        file.setDaily(this);
    }

    public void update(String title, String list, String fileName, String filePath) {
        this.title = title;
        this.list = list;
    }

    public void setUpdateData(DailyDto dailyDto, List<FileInfo> fileInfoList) {
        this.id = dailyDto.getId();
        this.title = dailyDto.getTitle();
        this.list = dailyDto.getList();
        // fileInfoList.stream().map(file -> {
        // this.fileInfoList.add(file);
        // file.setDaily(this);
        // });
        this.fileInfoList.clear();
        for (FileInfo fileInfo : fileInfoList) {
            this.fileInfoList.add(fileInfo);
            fileInfo.setDaily(this);
        }
    }
}
