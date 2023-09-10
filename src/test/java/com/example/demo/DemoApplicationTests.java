package com.example.demo;

import com.example.demo.dao.AlphaDao;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.text.SimpleDateFormat;
@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes = DemoApplication.class)
class DemoApplicationTests implements ApplicationContextAware {
    private ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;   //得到这个spring容器

    }

    @Test
    public void testApplicationContext(){
        AlphaDao bean = applicationContext.getBean(AlphaDao.class); //根据类型查找容器里面对应的bean（java对象）
        System.out.println(bean.select());

        applicationContext.getBean("aplhabernate",AlphaDao.class);

        SimpleDateFormat bean1 = applicationContext.getBean(SimpleDateFormat.class);


    }

    @Autowired   //spring容器把AlphaDao注入给这个属性
    @Qualifier("aplhabernate")  //如果该接口有两个实现类，可以加入这个注解表示选择哪个bean注入给这个属性
    private AlphaDao alphaDao;

    @Test
    public void testdi(){
        System.out.println(alphaDao);
    }
}
