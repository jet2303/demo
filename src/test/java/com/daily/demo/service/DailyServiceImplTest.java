package com.daily.demo.service;

import org.mockito.InjectMocks;
import org.mockito.Spy;

import org.springframework.boot.test.context.SpringBootTest;

import com.daily.demo.repository.DailyRepositoryTest;

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
