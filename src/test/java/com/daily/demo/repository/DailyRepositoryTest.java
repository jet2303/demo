package com.daily.demo.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
import java.util.ArrayList;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import com.daily.demo.config.ConfigService;
import com.daily.demo.config.QueryDslConfig;
import com.daily.demo.dto.DailyDto;
import com.daily.demo.entity.daily.Daily;
import com.daily.demo.entity.daily.enumData.Useyn;
import com.daily.demo.util.DatabaseCleanUp;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

@DataJpaTest
@Import(value = { QueryDslConfig.class, ConfigService.class })
public class DailyRepositoryTest {

    @Autowired
    DailyRepository dailyRepository;

    @Autowired
    DatabaseCleanUp databaseCleanUp;

    MockMultipartFile file1;

    @BeforeEach
    void init() {
        databaseCleanUp.execute();

        Daily daily1 = new Daily(null, "title1", "list1", Useyn.Y, null);
        Daily daily2 = new Daily(null, "title2", "list2", Useyn.Y, null);
        Daily daily3 = new Daily(null, "title3", "list3", Useyn.Y, null);
        Daily daily4 = new Daily(null, "title4", "list4", Useyn.Y, null);

        MockMultipartFile file1;
        List<MultipartFile> fileInfos = new ArrayList<>();
        try {
            file1 = new MockMultipartFile("testimage",
                    "image2.jpg",
                    "image/jpeg",
                    // new FileInputStream(new File(filePath + "\\" + fileName)));
                    new FileInputStream(
                            new File("C:\\Users\\Su\\Desktop\\demo\\src\\test\\resources\\images\\image1.jpg")));

            fileInfos.add((MultipartFile) file1);
        } catch (FileNotFoundException fe) {
            fe.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        dailyRepository.save(daily1);
        dailyRepository.save(daily2);
        dailyRepository.save(daily3);
        dailyRepository.save(daily4);

    }

    @Test
    void select() {

        // List<DailyDto> queryResult = dailyRepository.findByDailyId(1L).get();
        Daily result = dailyRepository.findById(1L).get();
        assertEquals(result.getTitle(), "title1");
        assertEquals(result.getList(), "list1");

    }

    @Test
    @Disabled
    void update() {

    }
}
