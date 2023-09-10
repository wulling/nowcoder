package com.example.demo.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.Date;

//AOP 面向方面（切面）编程，是一种编程思想
//spring AOP在运行时通过代理的方式织入代码，只支持方法类型的连接点
//JDK动态代理 运行时创建接口的代理实例   CGLib动态代理 在运行时创建子类代码实例（在目标对象不存在接口时，spring AOP会采用此种方式）


//@Component
//@Aspect
public class AlphaAspect {
    @Pointcut("execution(* com.example.demo.service.*.*(..))")  //声明代码织入到哪些对象的哪些位置上
    public void pointcut(){
    }


    //处理的逻辑

    @Before("pointcut()")
    public void before(){
        System.out.println("before");
    }
    @After("pointcut()")
    public void after(){
        System.out.println("after");
    }

    @AfterReturning("pointcut()")  //返回值之后
    public void afterReturning(){
        System.out.println("afterReturning");
    }

    @AfterThrowing("pointcut()")
    public void afterThrowing(){
        System.out.println("afterThrowing");
    }

    @Around("pointcut()") //前后都织入
    //Spring AOP 中的 ProceedingJoinPoint 在获取接口或方法参数时是按照方法参数的顺序获取的。 joinpoint:目标对象上织入代码的位置
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        System.out.println("around before");
        Object obj = joinPoint.proceed(); //调用目标组件的那个方法
        System.out.println("around after");
        return obj;
    }


}
