package com.example.demo.dao;

import org.springframework.stereotype.Repository;

@Repository("aplhabernate")  //bean默认名字是类名首字母小写，也可以自定义bean的名字，在该注解里面写字符串就是bean自定义的名字
public class AlphaDaoimp implements AlphaDao{
    @Override
    public String select() {
        return "hibernate";
    }
}
