package com.example.demo.config;

import com.google.code.kaptcha.Producer;
import com.google.code.kaptcha.impl.DefaultKaptcha;
import com.google.code.kaptcha.util.Config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Properties;

@Configuration
public class KaptchaConfig {
    @Bean
    public Producer kaptchaProducer(){
        Properties properties = new Properties();
        properties.setProperty("Kaptcha.image.width","100");
        properties.setProperty("Kaptcha.image.height","40");
        properties.setProperty("Kaptcha.testproducer.font.size","32");
        properties.setProperty("Kaptcha.testproducer.font.color","0,0,0");
        properties.setProperty("Kaptcha.testproducer.char.string","0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ");
        properties.setProperty("Kaptcha.testproducer.char.length","4");
        properties.setProperty("Kaptcha.noise.impl","com.google.code.Kaptcha.impl.NoNoise");
        DefaultKaptcha kaptcha = new DefaultKaptcha();
        Config config = new Config(properties);
        kaptcha.setConfig(config);
        return kaptcha;
    }

}
