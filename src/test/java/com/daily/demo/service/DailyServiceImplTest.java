package com.daily.demo.service;

import org.mockito.InjectMocks;
import org.mockito.Spy;

import org.springframework.boot.test.context.SpringBootTest;

import com.daily.demo.repository.DailyRepositoryTest;
import com.daily.demo.service.daily.DailyServiceImpl;

// @DataJpaTest
@SpringBootTest
public class DailyServiceImplTest {

    @InjectMocks
    // @Autowired
    private DailyServiceImpl dailyServiceImpl;

    @Spy
    // @Autowired
    private DailyRepositoryTest dailyRepository;

}
