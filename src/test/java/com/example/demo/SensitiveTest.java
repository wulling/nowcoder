package com.example.demo;

import com.example.demo.util.SensitiveFilter;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes = DemoApplication.class)
public class SensitiveTest {

    @Autowired
    private SensitiveFilter sensitiveFilter;
    @Test
    public void test1(){
//        String text = "这里可以赌博，可以吸毒，可以开票，可以嫖娼，哈哈哈！";
        String text2 = "abcefrgfabc";
//        text = sensitiveFilter.filter(text);
//        System.out.println(text);
        text2 = sensitiveFilter.filter(text2);
        System.out.println(text2);


    }


}
