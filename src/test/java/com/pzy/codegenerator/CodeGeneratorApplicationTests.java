package com.pzy.codegenerator;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;


@SpringBootTest
class CodeGeneratorApplicationTests {
    @Test
    void contextLoads() {
        // 获取当前日期
        LocalDate currentDate = LocalDate.now();
        // 获取当前年月
        int currentDateYear = currentDate.getYear();
        int currentDateMonthValue = currentDate.getMonthValue();
        String date = currentDateYear + "-" + String.format("%02d", currentDateMonthValue);

        System.err.println(date);
    }

}