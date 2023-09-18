package com.pzy.codegenerator;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class CodeGeneratorApplication {

    public static void main(String[] args) {
        SpringApplication.run(CodeGeneratorApplication.class, args);
    }

}
