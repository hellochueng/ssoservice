package com.sso.service;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.ImportResource;

@SpringBootApplication
@EnableCaching
@MapperScan("com.sso.service.dao")
@ImportResource(locations = {"classpath:dubbo.xml"})
public class Application {

    public static void main(String args[]){
            SpringApplication.run(Application.class, args);
    }

}
