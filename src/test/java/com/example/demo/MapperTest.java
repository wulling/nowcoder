package com.example.demo;

import com.example.demo.dao.DiscussPostMapper;
import com.example.demo.dao.LoginTicketMapper;
import com.example.demo.dao.MessageMapper;
import com.example.demo.dao.UserMapper;
import com.example.demo.entity.DiscussPost;
import com.example.demo.entity.LoginTicket;
import com.example.demo.entity.Message;
import com.example.demo.entity.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;
import java.util.List;
@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes = DemoApplication.class)
public class MapperTest {

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private DiscussPostMapper discussPostMapper;

    @Autowired
    private LoginTicketMapper loginTicketMapper;

    @Autowired
    private MessageMapper messageMapper;

    @Test
    public void test1(){
        User user = userMapper.selectById(101);
        System.out.println(user);
        int i = userMapper.updatePassword(111, "1234");
        System.out.println(i);

    }
    @Test
    public void test2(){
        List<DiscussPost> discussPosts = discussPostMapper.selectDiscussPosts(149, 0, 10,0);
        for(DiscussPost dis : discussPosts){
            System.out.println(dis);
        }
        int rows = discussPostMapper.selectDiscussPostRows(149);
        System.out.println(rows);
    }
    @Test
    public void test3(){
        LoginTicket loginTicket = new LoginTicket();
        loginTicket.setUserId(101);
        loginTicket.setTicket("abc");
        loginTicket.setStatus(0);
        loginTicket.setExpired(new Date(System.currentTimeMillis()+1000*60*10));
        loginTicketMapper.insertLoginTicket(loginTicket);

        LoginTicket loginTicket1 = loginTicketMapper.selectByTicket("abc");
        loginTicketMapper.updateStatus("abc",1);

    }

    @Test
    public void test4(){
        for (Message conversation : messageMapper.selectConversations(111, 0, 20)) {
            System.out.println(conversation);
        }

        int count = messageMapper.selectConversationCount(111);
        System.out.println(count);

        for (Message selectLetter : messageMapper.selectLetters("111_112", 0, 10)) {
            System.out.println(selectLetter);
        }

        count = messageMapper.selectLetterCount("111_112");
        System.out.println(count);

        int i = messageMapper.selectLetterUnreadCount(131, "111_131");
        System.out.println(i);


    }
}
