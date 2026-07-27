package com.quotation.interfaces;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "com.quotation")
@MapperScan("com.quotation.infrastructure.persistence.mapper")
public class QuotationApplication {

    public static void main(String[] args) {
        SpringApplication.run(QuotationApplication.class, args);
        System.out.println("=================================");
        System.out.println("报价议价系统启动成功!");
        System.out.println("API 地址：http://localhost:8080/api/quotation");
        System.out.println("=================================");
    }
}
