package com.example.demo;

import com.example.demo.service.alphaservice;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@SpringBootTest
@RunWith(SpringRunner.class)
@ContextConfiguration(classes = DemoApplication.class)
public class ThreadPoolTest {
    private static final Logger logger = LoggerFactory.getLogger(ThreadPoolTest.class);

    //JDK普通线程池
    private ExecutorService executorService = Executors.newFixedThreadPool(5);

    //JDK可执行定时任务的线程池
    private ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(5);

    //spring普通线程池
    @Autowired
    private ThreadPoolTaskExecutor threadPoolTaskExecutor;

    //spring可执行定时任务的线程池
    @Autowired
    private ThreadPoolTaskScheduler threadPoolTaskScheduler;
    @Autowired
    private alphaservice alphaservice;

    private void sleep(long m){
        try {
            Thread.sleep(m);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    //1.JDK普通线程池
    @Test
    public void testExecutorService(){
        Runnable task = new Runnable() {
            @Override
            public void run() {
                logger.debug("hello ExecutorService");
            }
        };

        for (int i = 0; i < 10; i++) {
            executorService.submit(task);
        }
        sleep(10000);
    }

    //2.JDK定时任务线程池
    @Test
    public void testScheduledExecutorService(){
        Runnable task = new Runnable() {
            @Override
            public void run() {
                logger.debug("hello ScheduledExecutorService");
            }
        };
        scheduledExecutorService.scheduleAtFixedRate(task,10000,1000, TimeUnit.MILLISECONDS);
        sleep(30000);
    }

    //3.spring普通线程池
    @Test
    public void testThreadPoolTaskExecutor(){
        Runnable task = new Runnable() {
            @Override
            public void run() {
                logger.debug("hello ThreadPoolTaskExecutor");
            }
        };

        for (int i = 0; i < 10; i++) {
            threadPoolTaskExecutor.submit(task);
        }
        sleep(10000);
    }

    //4.spring定时任务线程池
    @Test
    public void testThreadPoolTaskScheduler(){
        Runnable task = new Runnable() {
            @Override
            public void run() {
                logger.debug("hello ThreadPoolTaskScheduler");
            }
        };
        Date start = new Date(System.currentTimeMillis()+10000);
        threadPoolTaskScheduler.scheduleAtFixedRate(task,start,1000);
        sleep(30000);
    }

    //5.spring普通线程池简化
    @Test
    public void testThreadPoolTaskExecutorSimple(){
        for (int i = 0; i < 10; i++) {
            alphaservice.execute1();
        }
        sleep(10000);
    }

    //6.spring定时任务线程池简化
    @Test
    public void testThreadPoolTaskSchedulerSimple(){
        sleep(30000);
    }

}
