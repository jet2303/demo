package com.daily.demo.entity.daily;

import static com.daily.demo.entity.daily.QDaily.daily;

import java.util.ArrayList;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.daily.demo.dto.DailyDto;
import com.daily.demo.dto.request.DailyRequest;
import com.daily.demo.entity.daily.enumData.Useyn;

import jakarta.annotation.Nonnull;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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
    @Nonnull
    @Column(name = "list")
    private String list;

    // 세부 하위의 세부 리스트
    // private String inList;

    @Column(name = "useYn")
    @Builder.Default
    @Enumerated(EnumType.STRING)
    private Useyn useYn = Useyn.Y;

    // 연관관계
    @Builder.Default
    @OneToMany(mappedBy = "daily", cascade = CascadeType.ALL)
    private List<FileInfo> fileInfoList = new ArrayList<>();

    public void addFile(FileInfo file) {
        this.fileInfoList.add(file);
        file.setDaily(this);
    }

    public void update(String title, String list, String fileName, String filePath) {
        this.title = title;
        this.list = list;
    }

    public Daily update(DailyRequest updateDaily, List<FileInfo> fileList) {
        // this.id = updateDaily.getId();
        this.title = updateDaily.getTitle();
        this.list = updateDaily.getList();
        this.useYn = updateDaily.getUseyn();
        this.fileInfoList.clear();
        for (FileInfo fileInfo : fileList) {
            this.fileInfoList.add(fileInfo);
            fileInfo.setDaily(this);
        }
        return this;
    }

    public void setUpdateData(DailyDto dailyDto, List<FileInfo> fileInfoList) {
        this.id = dailyDto.getId();
        this.title = dailyDto.getTitle();
        this.list = dailyDto.getList();
        this.useYn = Useyn.Y;
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

    public void delete() {
        this.useYn = Useyn.N;
    }

    public void delete(Useyn useyn) {
        this.useYn = useyn;
    }
}
