package com.daily.demo.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import com.daily.demo.config.ConfigService;
import com.daily.demo.dto.DailyDto;
import com.daily.demo.dto.response.DailyResponse;
import com.daily.demo.entity.daily.Daily;
import com.daily.demo.repository.DailyRepository;
import com.daily.demo.util.DatabaseCleanUp;

import jakarta.persistence.EntityManagerFactory;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

@SpringBootTest
@Import(value = { ConfigService.class })
// @Import(value = { QueryDslConfig.class, ConfigService.class })
@Slf4j
public class QueryDslTest {

    // @Autowired
    // JPAQueryFactory jpaQueryFactory;

    // @Spy
    @Autowired
    DailyRepository dailyRepository;

    // @InjectMocks
    @Autowired
    DailyServiceImpl dailyService;

    @Autowired
    DatabaseCleanUp databaseCleanUp;

    @Autowired
    EntityManagerFactory entityManagerFactory;

    // bulk 연산 후 영속성 컨텍스트를 초기화 위해서 사용
    // @Autowired
    // EntityManager em;

    @Value(value = "${testfile.path}")
    private String filePath;

    @Value(value = "${testfile.name}")
    private String fileName;

    // private List<MultipartFile> fileInfos = new ArrayList<>();

    // 초기 데이터 설정
    @BeforeEach
    void init() {
        databaseCleanUp.execute();

        Daily daily1 = new Daily(null, "title1", "list1", null);
        Daily daily2 = new Daily(null, "title2", "list2", null);
        Daily daily3 = new Daily(null, "title3", "list3", null);
        Daily daily4 = new Daily(null, "title4", "list4", null);

        dailyRepository.save(daily1);
        dailyRepository.save(daily2);
        dailyRepository.save(daily3);
        dailyRepository.save(daily4);

    }

    @Test
    @Transactional
    void create() {
        // given
        MockMultipartFile file1, file2;
        List<MultipartFile> fileInfos = new ArrayList<>();

        try {
            file1 = new MockMultipartFile("testimage",
                    "image1.jpg",
                    "image/jpeg",
                    // new FileInputStream(new File(filePath + "\\" + fileName)));
                    new FileInputStream(
                            new File("C:\\Users\\Su\\Desktop\\demo\\src\\test\\resources\\images\\image1.jpg")));

            file2 = new MockMultipartFile("testimage",
                    "image2.jpg",
                    "image/jpeg",
                    // new FileInputStream(new File(filePath + "\\" + fileName)));
                    new FileInputStream(
                            new File("C:\\Users\\Su\\Desktop\\demo\\src\\test\\resources\\images\\image2.jpg")));

            fileInfos.add((MultipartFile) file1);
            fileInfos.add((MultipartFile) file2);
        } catch (FileNotFoundException fe) {
            fe.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        DailyDto dto = DailyDto.builder()
                .id(null)
                .title("create title")
                .list("create list")
                .build();

        // when
        Long result = dailyService.create(dto, fileInfos);

        DailyResponse response = dailyService.read(result);
        assertEquals(response.getId(), 5L);
        assertEquals(response.getFileList().size(), 2);

    }

    @Test
    @Disabled
    void read() {
        DailyResponse response = dailyService.read(5L);

        assertEquals(response.getTitle(), "title1");
        assertEquals(response.getFileList(), 2);

    }

    @Test
    void update() {
        MockMultipartFile file1, file2, file3, file4;
        List<MultipartFile> fileInfos = new ArrayList<>();

        try {
            file1 = new MockMultipartFile("testimage",
                    "image1.jpg",
                    "image/jpeg",
                    // new FileInputStream(new File(filePath + "\\" + fileName)));
                    new FileInputStream(
                            new File("C:\\Users\\Su\\Desktop\\demo\\src\\test\\resources\\images\\image1.jpg")));

            file2 = new MockMultipartFile("testimage",
                    "image2.jpg",
                    "image/jpeg",
                    // new FileInputStream(new File(filePath + "\\" + fileName)));
                    new FileInputStream(
                            new File("C:\\Users\\Su\\Desktop\\demo\\src\\test\\resources\\images\\image2.jpg")));

            fileInfos.add((MultipartFile) file1);
            fileInfos.add((MultipartFile) file2);
        } catch (FileNotFoundException fe) {
            fe.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        DailyDto dto = DailyDto.builder()
                .id(null)
                .title("create title")
                .list("create list")
                .build();

        Long result = dailyService.create(dto, fileInfos);

        //////////////////////////////////////////////////////////////////
        fileInfos.clear();
        log.info("before : {}", dailyService.read(result));
        try {
            file3 = new MockMultipartFile("testimage",
                    "test1.png",
                    "image/png",
                    new FileInputStream(
                            new File("C:\\Users\\Su\\Desktop\\demo\\src\\test\\resources\\images\\test1.png")));

            file4 = new MockMultipartFile("testimage",
                    "test2.png",
                    "image/png",
                    new FileInputStream(
                            new File("C:\\Users\\Su\\Desktop\\demo\\src\\test\\resources\\images\\test2.png")));

            fileInfos.add((MultipartFile) file3);
            fileInfos.add((MultipartFile) file4);
        } catch (FileNotFoundException fe) {
            fe.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        DailyDto updateDto = DailyDto.builder()
                .id(result)
                .title("update title")
                .list("update list")
                .build();

        Long ret = dailyService.update(updateDto, fileInfos);

        log.info("after : {}", dailyService.read(ret));
        assertEquals(ret, 5L);
    }
}
