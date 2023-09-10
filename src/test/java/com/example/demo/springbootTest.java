package com.example.demo;

import com.example.demo.entity.DiscussPost;
import com.example.demo.service.DiscussPostService;
import org.junit.*;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.Period;
import java.util.Date;

@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes = DemoApplication.class)
public class springbootTest {

    @Autowired
    private DiscussPostService discussPostService;

    private DiscussPost post;

    @BeforeClass  //类初始化之前执行
    public static void beforeClass(){
        System.out.println("beforeclass");
    }

    @AfterClass  //类销毁的时候执行
    public static void afterClass(){
        System.out.println("afterclass");
    }

    @Before  //调用任何测试方法之前都被执行
    public void before(){
        System.out.println("before");
        //初始测试数据
        post = new DiscussPost();
        post.setUserId(111);
        post.setTitle("Test title");
        post.setContent("Tets content");
        post.setCreateTime(new Date());
        discussPostService.addDiscussPost(post);
    }

    @After //调用任何测试方法之后都被执行
    public void after(){
        System.out.println("after");
        //删除测试数据
        discussPostService.updateStatus(post.getId(),2);
    }

    @Test
    public void test1(){
        System.out.println("test1");
    }

    @Test
    public void test2(){
        DiscussPost post1 = discussPostService.findDiscussPostById(this.post.getId());
        Assert.assertNotNull(post1);
        Assert.assertEquals(post.getTitle(),post1.getTitle());
    }

    @Test
    public void test3(){
        int i = discussPostService.updateScore(post.getId(), 200.00);
        DiscussPost post1 = discussPostService.findDiscussPostById(this.post.getId());
        Assert.assertEquals(200.00,post1.getScore(),2);
    }


}
