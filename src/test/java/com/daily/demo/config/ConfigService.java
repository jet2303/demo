package com.daily.demo.config;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

import com.daily.demo.util.DatabaseCleanUp;

@TestConfiguration
public class ConfigService {

    // @Bean
    // public DailyServiceImpl dailyRepository(){
    // return new DailyServiceImpl(null)
    // }

    @Bean
    public DatabaseCleanUp databaseCleanUp() {
        return new DatabaseCleanUp();
    }

}
