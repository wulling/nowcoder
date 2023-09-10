package com.example.demo;

import com.example.demo.config.ReidsConfig;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisStringCommands;
import org.springframework.data.redis.core.*;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.concurrent.TimeUnit;
@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes = DemoApplication.class)
public class RedisTests {
    @Autowired
    private RedisTemplate redisTemplate;

    @Test
    public void test1(){
        String redisKey = "test:count";
        redisTemplate.opsForValue().set(redisKey,1);
        System.out.println(redisTemplate.opsForValue().get(redisKey));
        System.out.println(redisTemplate.opsForValue().increment(redisKey));
        System.out.println(redisTemplate.opsForValue().decrement(redisKey));

        redisKey = "test:user";
        redisTemplate.opsForHash().put(redisKey,"id",1);
        redisTemplate.opsForHash().put(redisKey,"username","张三");
        System.out.println(redisTemplate.opsForHash().get(redisKey, "id"));

        redisKey = "test:ids";
        redisTemplate.opsForList().leftPush(redisKey,101);
        redisTemplate.opsForList().leftPush(redisKey,102);
        redisTemplate.opsForList().leftPush(redisKey,103);
        System.out.println(redisTemplate.opsForList().size(redisKey));
        System.out.println(redisTemplate.opsForList().leftPop(redisKey));
        System.out.println(redisTemplate.opsForList().index(redisKey,0));
        System.out.println(redisTemplate.opsForList().range(redisKey,0,2));

        redisKey = "test:teachers";
        redisTemplate.opsForSet().add(redisKey,101,102,103);
        System.out.println(redisTemplate.opsForSet().size(redisKey));
        System.out.println(redisTemplate.opsForSet().pop(redisKey));
        System.out.println(redisTemplate.opsForSet().members(redisKey));

        redisKey = "test:students";
        redisTemplate.opsForZSet().add(redisKey,"刘备",20);
        redisTemplate.opsForZSet().add(redisKey,"诸葛亮",50);
        redisTemplate.opsForZSet().add(redisKey,"曹操",70);
        System.out.println(redisTemplate.opsForZSet().zCard(redisKey));
        System.out.println(redisTemplate.opsForZSet().score(redisKey,"刘备"));
        System.out.println(redisTemplate.opsForZSet().rank(redisKey,"诸葛亮"));
        System.out.println(redisTemplate.opsForZSet().range(redisKey,0,2));

        redisTemplate.delete("test:user");
        System.out.println(redisTemplate.hasKey("test:user"));
        redisTemplate.expire("test:students",5, TimeUnit.SECONDS);



    }

    //多次访问同一个key
    @Test
    public void test2(){
        String redisKey = "test:count";
        BoundValueOperations operations = redisTemplate.boundValueOps(redisKey);
        operations.decrement();
        System.out.println(operations.get());
    }

    //编程式事务
    @Test
    public void test3(){
        Object obj = redisTemplate.execute(new SessionCallback() {
            @Override
            public Object execute(RedisOperations operations) throws DataAccessException {
                String redisKey = "test:tx";
                operations.multi();  //开启事务  redis的事务管理是将命令放在一个队列里，等提交事务之后统一执行
                operations.opsForSet().add(redisKey,101);
                operations.opsForSet().add(redisKey,102);

                return operations.exec();  //提交事务
            }
        });

        System.out.println(obj);
    }

    //统计20万个重复数据的独立总数
    @Test
    public void testHyperloglog(){
        String redisKey = "test:hll:01";
        for (int i = 0; i < 100000; i++) {
            redisTemplate.opsForHyperLogLog().add(redisKey,i);
        }
        for (int i = 0; i < 100000; i++) {
            int r = (int)Math.random()*100000+1;
            redisTemplate.opsForHyperLogLog().add(redisKey,r);
        }
        Long size = redisTemplate.opsForHyperLogLog().size(redisKey);
        System.out.println(size);
    }

    //将三组数据合并，再统计合并后的重复数据的独立总数
    @Test
    public void testHyperloglogUnion(){
        String redisKey2 = "test:hll:02";
        for (int i = 0; i < 10000; i++) {
            redisTemplate.opsForHyperLogLog().add(redisKey2,i);
        }
        String redisKey3 = "test:hll:03";
        for (int i = 5001; i < 15000; i++) {
            redisTemplate.opsForHyperLogLog().add(redisKey3,i);
        }
        String redisKey4 = "test:hll:04";
        for (int i = 10001; i < 20000; i++) {
            redisTemplate.opsForHyperLogLog().add(redisKey4,i);;
        }
        String unionKey = "test:hll:union";
        redisTemplate.opsForHyperLogLog().union(unionKey,redisKey2,redisKey3,redisKey4);
        System.out.println(redisTemplate.opsForHyperLogLog().size(unionKey));
    }

    //统计一组数据的布尔值
    @Test
    public void testBitMap(){
        String redisKey = "test:bm:01";

        //记录
        redisTemplate.opsForValue().setBit(redisKey,1,true);
        redisTemplate.opsForValue().setBit(redisKey,4,true);
        redisTemplate.opsForValue().setBit(redisKey,7,true);
        //查询
        redisTemplate.opsForValue().getBit(redisKey,1);
        redisTemplate.opsForValue().getBit(redisKey,2);
        //统计
        Object obj = redisTemplate.execute(new RedisCallback() {
            @Override
            public Object doInRedis(RedisConnection redisConnection) throws DataAccessException {
                return  redisConnection.bitCount(redisKey.getBytes());
            }
        });
        System.out.println(obj);   //3
    }

    //统计三组数据的布尔值，并对这三组数据做OR运算
    @Test
    public void testBitMapOperation(){
        String redisKey2 = "test:bm:02";
        redisTemplate.opsForValue().setBit(redisKey2,0,true);
        redisTemplate.opsForValue().setBit(redisKey2,1,true);
        redisTemplate.opsForValue().setBit(redisKey2,2,true);
        String redisKey3 = "test:bm:03";
        redisTemplate.opsForValue().setBit(redisKey3,2,true);
        redisTemplate.opsForValue().setBit(redisKey3,3,true);
        redisTemplate.opsForValue().setBit(redisKey3,4,true);
        String redisKey4 = "test:bm:04";
        redisTemplate.opsForValue().setBit(redisKey4,4,true);
        redisTemplate.opsForValue().setBit(redisKey4,5,true);
        redisTemplate.opsForValue().setBit(redisKey4,6,true);

        String redisKey = "test:bm:or";
        Object obj = redisTemplate.execute(new RedisCallback() {
            @Override
            public Object doInRedis(RedisConnection redisConnection) throws DataAccessException {
                redisConnection.bitOp(RedisStringCommands.BitOperation.OR,redisKey.getBytes(),redisKey2.getBytes(),
                        redisKey3.getBytes(),redisKey4.getBytes());

                return redisConnection.bitCount(redisKey.getBytes());
            }
        });
        System.out.println(obj);
    }
}
