package com.example.demo;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes = DemoApplication.class)
public class KafkaTest {
    @Autowired
    private KafkaProducer kafkaProducer;
    @Test
    public void test1(){
        kafkaProducer.sendMessage("test","你好");
        kafkaProducer.sendMessage("test","在吗");
        try {
            Thread.sleep(1000*10);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

    }
}
//我们主动调用生产者发消息，而消费者处理消息是被动自动调的
@Component
class KafkaProducer{
    @Autowired
    private KafkaTemplate kafkaTemplate;

    public void sendMessage(String topic,String content){
        kafkaTemplate.send(topic,content);
    }
}

@Component
class KafkaConsumer{
    @KafkaListener(topics = ("test"))
    public void handleMessage(ConsumerRecord record){
        System.out.println(record.value());
    }
}
