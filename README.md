#仿牛客网社区网页开发项目

  这个项目的整体结构来源于牛客网，主要模块：用户模块、权限模块、点赞评论关注模块、置顶加精删除模块、私信模块、查询模块、通知模块、热帖排行模块、网站用户统计模块。用户注册通过邮箱进行验证激活才能使用，用户登录将验证码存到了redis提高性能，使用了Spring Security用于管理项目中的登录权限，将点赞和关注的数据也存入redis，
  应用了消息队列的Kakfa实现系统通知，前缀树算法过滤敏感词，使用了Elasticsearch实现搜索博客，使用 Quartz定时任务刷新热帖，使用了caffeine给帖子做缓存提升性能。 
  -技术栈：SpringBoot、Mybatis、Spring Security、Redis、Kafka、 Elasticsearch、Quartz

#开发环境

  构建工具：Apache Maven
  集成开发工具：IDEA
  数据库：MySQL、Redis
  应用服务器：Apache Tomcat
  版本控制工具：Git

#项目亮点

    项⽬构建在 Spring Boot+SSM 框架之上，并统⼀的进⾏了状态管理、事务管理、异常处理；
    
    利⽤ Redis 实现了点赞和关注功能；
    
    利⽤ Kafka 实现了异步的站内通知；
    
    利⽤ Elasticsearch 实现了全⽂搜索功能，可准确匹配搜索结果，并⾼亮显示关键词；
    
    利⽤ Caffeine+Redis 实现了两级缓存，并优化了热⻔帖⼦的访问。
    
    利⽤ Spring Security 实现了权限控制，实现了多重⻆⾊、URL 级别的权限管理；
    
    利⽤ HyperLogLog、Bitmap 分别实现了 UV、DAU 的统计功能，100 万⽤户数据只需*M 内存空间；
    
    利⽤ Quartz 实现了任务调度功能，并实现了定时计算帖⼦分数、定时清理垃圾⽂件等功能；
    
    利⽤ Actuator 对应⽤的 Bean、缓存、⽇志、路径等多个维度进⾏了监控，并通过⾃定义的端点对数据库连接进⾏了监控。

#运行

    安装JDK，Maven
    克隆代码到本地  git clone https://github.com/zhengguohuang/community.git
    配置mysql、七牛云、kafka、ElasticSearch
    启动zookeeper
    启动Kafka
    启动Elasticsearch
    运行打包命令  mvn package
    运行项目    java -jar xxx.jar
    访问项目   http://localhost:8080
  
# DEMO




    
