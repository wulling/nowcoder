package com.example.demo;

import com.example.demo.util.MailClient;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes = DemoApplication.class)
public class MailTest {
    @Autowired
    private MailClient mailClient;

    @Autowired
    private TemplateEngine templateEngine;

    @Test
    public void test1(){
        mailClient.sendMail("1072933084@qq.com","TEST","Welcome");
    }

    @Test
    public void test2(){
        Context context = new Context();
        context.setVariable("username","wuling");
        String htmlcontent = templateEngine.process("/mail/demo", context);
        System.out.println(htmlcontent);
        mailClient.sendMail("1072933084@qq.com","HTML",htmlcontent);

    }



}
