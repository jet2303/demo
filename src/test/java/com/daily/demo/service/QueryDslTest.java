package com.daily.demo.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
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
import com.daily.demo.dto.Header;
import com.daily.demo.dto.request.DailyRequest;
import com.daily.demo.dto.response.DailyResponse;
import com.daily.demo.entity.daily.Daily;
import com.daily.demo.entity.daily.FileInfo;
import com.daily.demo.entity.daily.enumData.Useyn;
import com.daily.demo.payload.error.ErrorCode;
import com.daily.demo.payload.error.errorCodes.DailyErrorCode;
import com.daily.demo.repository.DailyRepository;
import com.daily.demo.service.daily.DailyServiceImpl;
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

    @Value(value = "${testsave.folder}")
    private String saveFolderPath;

    // private List<MultipartFile> fileInfos = new ArrayList<>();

    // 초기 데이터 설정
    @BeforeEach
    void init() {
        databaseCleanUp.execute();

        MockMultipartFile file1, file2;
        List<MultipartFile> fileInfos = new ArrayList<>();

        try {
            file1 = new MockMultipartFile("testimage",
                    "image1.jpg",
                    "image/jpeg",
                    // new FileInputStream(new File(filePath + "\\" + fileName)));
                    new FileInputStream(
                            new File(
                                    "C:\\Users\\Su\\Desktop\\todoapp\\demo\\src\\test\\resources\\images\\image1.jpg")));

            file2 = new MockMultipartFile("testimage",
                    "image2.jpg",
                    "image/jpeg",
                    // new FileInputStream(new File(filePath + "\\" + fileName)));
                    new FileInputStream(
                            new File(
                                    "C:\\Users\\Su\\Desktop\\todoapp\\demo\\src\\test\\resources\\images\\image2.jpg")));

            fileInfos.add((MultipartFile) file1);
            fileInfos.add((MultipartFile) file2);
        } catch (FileNotFoundException fe) {
            fe.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        List<FileInfo> fileList1 = multipartToFileInfo(fileInfos);
        List<FileInfo> fileList2 = multipartToFileInfo(fileInfos);

        Daily daily1 = new Daily(null, "title1", "list1", Useyn.Y, fileList1);
        Daily daily2 = new Daily(null, "title2", "list2", Useyn.Y, fileList2);
        // Daily daily3 = new Daily(null, "title3", "list3", Useyn.Y, fileList1);
        // Daily daily4 = new Daily(null, "title4", "list4", Useyn.Y, fileList1);

        DailyRequest request1 = DailyRequest.builder()
                .title(daily1.getTitle())
                .list(daily1.getList())
                .useyn(Useyn.Y)
                .fileList(fileInfos)
                .build();
        DailyRequest request2 = DailyRequest.builder()
                .title(daily2.getTitle())
                .list(daily2.getList())
                .useyn(Useyn.Y)
                .fileList(fileInfos)
                .build();
        // DailyRequest request3 = DailyRequest.builder()
        // .title(daily3.getTitle())
        // .list(daily3.getList())
        // .useyn(Useyn.Y)
        // .fileList(fileInfos)
        // .build();
        // DailyRequest request4 = DailyRequest.builder()
        // .title(daily4.getTitle())
        // .list(daily4.getList())
        // .useyn(Useyn.Y)

        // .fileList(fileInfos)
        // .build();

        dailyService.create(Header.OK(request1));
        dailyService.create(Header.OK(request2));

    }

    @AfterEach
    public void close() {
        dailyRepository.deleteAll();
    }

    @AfterAll
    private static void deleteFile() {
        String filePath = "C:\\Users\\Su\\Desktop\\todoapp\\demo\\src\\test\\resources\\savefolder\\";
        File deleteFolder = new File(filePath);
        File[] deleteFileList = deleteFolder.listFiles();
        for (File file : deleteFileList) {
            file.delete();
        }
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

    @Test
    void read_fetch() {
        Daily queryResult = dailyRepository.fetchQuery(1L);
        log.info("fetch : {}", queryResult.toString());
        assertEquals(2, queryResult.getFileInfoList().size());
    }

    @Test
    void read_queryDsl() {
        Header<DailyResponse> queryResult = dailyService.read(1L);
        log.info("queryResult : {}", queryResult.getData().toString());
        assertEquals(2, queryResult.getData().getFileList().size());
    }

    @Test
    void update() {

        MultipartFile updateFile1, updateFile2;
        List<MultipartFile> fileList = new ArrayList<>();
        try {
            updateFile1 = new MockMultipartFile("testimage",
                    "test1.png",
                    "image/png",
                    // new FileInputStream(new File(filePath + "\\" + fileName)));
                    new FileInputStream(
                            new File(
                                    "C:\\Users\\Su\\Desktop\\todoapp\\demo\\src\\test\\resources\\images\\test1.png")));

            updateFile2 = new MockMultipartFile("testimage",
                    "test2.png",
                    "image/png",
                    // new FileInputStream(new File(filePath + "\\" + fileName)));
                    new FileInputStream(
                            new File(
                                    "C:\\Users\\Su\\Desktop\\todoapp\\demo\\src\\test\\resources\\images\\test2.png")));
            fileList.add(updateFile1);
            fileList.add(updateFile2);
        } catch (FileNotFoundException fe) {
            fe.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        Header<DailyRequest> request = Header.OK(DailyRequest.builder()
                .id(1L)
                .title("update title")
                .list("update list")
                .useyn(Useyn.Y)
                .fileList(fileList)
                .build());

        dailyService.update(request);

        // Daily result = dailyRepository.findById(1L).get();
        Daily result = dailyRepository.fetchQuery(1L);
        assertEquals("update title", result.getTitle());
        assertEquals("update list", result.getList());
        assertEquals("test1.png", result.getFileInfoList().get(0).getFileName());
        assertEquals("test2.png", result.getFileInfoList().get(1).getFileName());
    }

    @Test
    void delete() {
        dailyService.delete(1L);
        Daily result = dailyRepository.fetchQuery(1L);
        assertEquals(Useyn.N, result.getUseYn());
    }

}
