package com.example.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.text.SimpleDateFormat;

@Configuration  //配置类
public class AlphaConfig {

    @Bean  //这个方法返回的对象将被装配到容器中，方法名就是bean名 (@bean可以装配任何bean)
    public SimpleDateFormat getSimpleDateFormat(){
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    }


}
