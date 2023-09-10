package com.example.demo.config;

import com.example.demo.quartz.AlphaJob;
import com.example.demo.quartz.ClearShareImageJob;
import com.example.demo.quartz.PostScoreRefreshJob;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.quartz.JobDetailFactoryBean;
import org.springframework.scheduling.quartz.SimpleTriggerFactoryBean;

//如果application中没有quartz配置的话，quartz是读取内存中的配置消息的，如果application中配置了相关消息，则quartz会将配置信息存到数据库中
@Configuration
public class QuartzConfig {

    //FactoryBean可简化Bean的实例化过程：
    //1、spring通过FactoryBean封装了Bean的实例化过程
    //2.将FactoryBean装配到spring容器中
    //3. 将FactoryBean注入给其他的Bean
    //4.该Bean得到的是FactoryBean所管理的对象实例

    //配置JobDetail
//    @Bean
    public JobDetailFactoryBean alphaJobDetail(){
        JobDetailFactoryBean factoryBean = new JobDetailFactoryBean();
        factoryBean.setJobClass(AlphaJob.class);
        factoryBean.setName("alphaJob"); //给任务取名
        factoryBean.setGroup("alphaJobGroup"); //给任务取组名,多个任务可为一组
        factoryBean.setDurability(true); //该任务是否持久保存
        factoryBean.setRequestsRecovery(true); //该任务是否可恢复
        return factoryBean;
    }

    //配置Trigger(SimpleTriggerFactoryBean,CronTriggerFactoryBean)
//    @Bean
    public SimpleTriggerFactoryBean alphaTrigger(JobDetail  alphaJobDetail){
        SimpleTriggerFactoryBean factoryBean = new SimpleTriggerFactoryBean();
        factoryBean.setJobDetail(alphaJobDetail);
        factoryBean.setName("alphaTrigger"); //给trigger（触发器）取名
        factoryBean.setGroup("alphaTriggerGroup");
        factoryBean.setRepeatInterval(3000); //执行任务的频率
        factoryBean.setJobDataMap(new JobDataMap()); //用new JobDataMap()来存Job的一些状态
        return factoryBean;
    }

    //刷新帖子分数
    @Bean
    public JobDetailFactoryBean postScoreRefreshJobDetail(){
        JobDetailFactoryBean factoryBean = new JobDetailFactoryBean();
        factoryBean.setJobClass(PostScoreRefreshJob.class);
        factoryBean.setName("postScoreRefreshJob"); //给任务取名
        factoryBean.setGroup("communityJobGroup"); //给任务取组名,多个任务可为一组
        factoryBean.setDurability(true); //该任务是否持久保存
        factoryBean.setRequestsRecovery(true); //该任务是否可恢复
        return factoryBean;
    }
    @Bean
    public SimpleTriggerFactoryBean postScoreRefreshTrigger(JobDetail postScoreRefreshJobDetail){
        SimpleTriggerFactoryBean factoryBean = new SimpleTriggerFactoryBean();
        factoryBean.setJobDetail(postScoreRefreshJobDetail);
        factoryBean.setName("postScoreRefreshTrigger"); //给trigger（触发器）取名
        factoryBean.setGroup("communityTriggerGroup");
        factoryBean.setRepeatInterval(1000*60*5); //执行任务的频率
        factoryBean.setJobDataMap(new JobDataMap()); //用new JobDataMap()来存Job的一些状态
        return factoryBean;
    }
    //删除分享的图片
    @Bean
    public JobDetailFactoryBean cleanShareImageJobDetail(){
        JobDetailFactoryBean factoryBean = new JobDetailFactoryBean();
        factoryBean.setJobClass(ClearShareImageJob.class);
        factoryBean.setName("cleanShareImageJob"); //给任务取名
        factoryBean.setGroup("communityJobGroup"); //给任务取组名,多个任务可为一组
        factoryBean.setDurability(true); //该任务是否持久保存
        factoryBean.setRequestsRecovery(true); //该任务是否可恢复
        return factoryBean;
    }
    @Bean
    public SimpleTriggerFactoryBean cleanShareImageTrigger(JobDetail cleanShareImageJobDetail){
        SimpleTriggerFactoryBean factoryBean = new SimpleTriggerFactoryBean();
        factoryBean.setJobDetail(cleanShareImageJobDetail);
        factoryBean.setName("cleanShareImageTrigger"); //给trigger（触发器）取名
        factoryBean.setGroup("communityTriggerGroup");
        factoryBean.setRepeatInterval(1000*60*4); //执行任务的频率
        factoryBean.setJobDataMap(new JobDataMap()); //用new JobDataMap()来存Job的一些状态
        return factoryBean;
    }
}
