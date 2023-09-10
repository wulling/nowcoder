package com.example.demo.service;

import com.example.demo.dao.AlphaDao;
import com.example.demo.dao.DiscussPostMapper;
import com.example.demo.dao.UserMapper;
import com.example.demo.entity.DiscussPost;
import com.example.demo.entity.User;
import com.example.demo.util.CommunityUtil;
//import com.google.protobuf.RpcCallback;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;

import javax.annotation.PostConstruct;
import java.util.Date;
import java.util.concurrent.TimeUnit;

@Service
//默认访问bean时，bean只会被实例化一次，如果需要每次访问都实例化时@scope("prototype")
public class alphaservice {

    @Autowired
    private AlphaDao alphaDao;

    @Autowired
    private DiscussPostMapper discussPostMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private TransactionTemplate transactionTemplate;

    private static final Logger logger = LoggerFactory.getLogger(alphaservice.class);


    @PostConstruct  //该注解表示该方法会在构造器之后调用
    public void init(){
        System.out.println("cc");
    }

//    REQUIRED :支持当前事务（外部事务），如果不存在则创建新事务
//    REQUIRES_NEW：创建一个新事务，并且暂停当前事务（外部事务）
//    NESTED：如果当前存在事务（外部事务），则嵌套在该事务中执行（独立的提交和回滚），否则就会和REQUIRED一样
    @Transactional(isolation = Isolation.READ_COMMITTED,propagation = Propagation.REQUIRED)
    public Object savel(){
        //新增用户
        User user = new User();
        user.setUsername("alpha");
        user.setCreateTime(new Date());
        user.setHeaderUrl("http://image.nowcoder.com/head/99t.png");
        user.setSalt(CommunityUtil.generateUUID().substring(0,5));
        user.setPassword(CommunityUtil.md5("123"+user.getSalt()));
        user.setEmail("alaha@qq.com");
        userMapper.insertUser(user);

        //新增帖子
        DiscussPost discussPost = new DiscussPost();
        discussPost.setCreateTime(new Date());
        discussPost.setContent("欢迎新人");
        discussPost.setTitle("欢迎贴");
        discussPost.setUserId(user.getId());
        discussPostMapper.insertDiscussPost(discussPost);

        Integer.valueOf("abc");
        return "ok";

    }

    public Object save2(){
        transactionTemplate.setIsolationLevel(TransactionDefinition.ISOLATION_READ_COMMITTED);
        transactionTemplate.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);

        return transactionTemplate.execute(new TransactionCallback<Object>() {
            @Override
            public Object doInTransaction(TransactionStatus status) {
                //新增用户
                User user = new User();
                user.setUsername("gentic");
                user.setCreateTime(new Date());
                user.setHeaderUrl("http://image.nowcoder.com/head/999t.png");
                user.setSalt(CommunityUtil.generateUUID().substring(0,5));
                user.setPassword(CommunityUtil.md5("123"+user.getSalt()));
                user.setEmail("gentic@qq.com");
                userMapper.insertUser(user);

                //新增帖子
                DiscussPost discussPost = new DiscussPost();
                discussPost.setCreateTime(new Date());
                discussPost.setContent("我是新人");
                discussPost.setTitle("新人帖");
                discussPost.setUserId(user.getId());
                discussPostMapper.insertDiscussPost(discussPost);

                Integer.valueOf("abc");
                return "ok";
            }
        });

    }

    @Async   //让该方法在多线程的环境下，被异步的调用
    public void execute1(){
        logger.debug("execute1");
    }
//    @Scheduled(initialDelay = 10000,fixedRate = 1000)
    public void execute2(){
        logger.debug("execute2");
    }

}
