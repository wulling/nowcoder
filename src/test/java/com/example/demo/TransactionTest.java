package com.example.demo;

import com.example.demo.service.alphaservice;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes = DemoApplication.class)
public class TransactionTest {

    @Autowired
    private alphaservice alph;

    @Test
    public void test1(){
        Object savel = alph.savel();

    }
    @Test
    public void test2(){
        Object o = alph.save2();
    }


}
