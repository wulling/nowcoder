package com.example.demo.config;

import com.example.demo.util.CommunityConstant;
import com.example.demo.util.CommunityUtil;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter implements CommunityConstant {
    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/resources/**");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        //授权
        http.authorizeRequests()
                .antMatchers("/user/setting",
                        "/user/upload",
                        "/discuss/add",
                        "/comment/add/**",
                        "/letter/**",
                        "/notice/**",
                        "/like",
                        "/follow",
                        "/unfollow"
                )
                .hasAnyAuthority(
                AUTHORITY_USER,
                AUTHORITY_ADMIN,
                AUTHORITY_MODERATOR
                )
                .antMatchers(
                        "/discuss/top",
                        "/discuss/wonderful"
                )
                .hasAnyAuthority(
                        AUTHORITY_MODERATOR
                )
                .antMatchers("/discuss/delete",
                                "/data/**",
                             "/actuator/**"
                )
                .hasAnyAuthority(AUTHORITY_ADMIN)
                .anyRequest().permitAll() //除了这些以外的任何请求都允许
                .and().csrf().disable();  //这里由于项目ajax请求多，偷懒将security的csrf配置取消了。（不推荐做法）
                   //为了防止CSRF攻击，security会在服务器传给浏览器form表单页面时带上token(随机生成的)
                     //然后浏览器发送form表单数据时带上这个token，服务器才会处理，否则就认为是CSRF攻击
                      //但是项目很多都是ajax请求，所以可以返回页面时强制生成一个key:value形式的token,
                     // 在浏览器发送ajax请求之前，将CSRF令牌设置到请求的消息头中。这样服务器接收到带有token的请求可以正常处理
        //权限不够时的处理
        http.exceptionHandling()
                .authenticationEntryPoint(new AuthenticationEntryPoint() {   //没有登录时的处理
                    @Override
                    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException e) throws IOException, ServletException {
                        String xRequestedWith = request.getHeader("x-requested-with");
                        if("XMLHttpRequest".equals(xRequestedWith)){
                            response.setContentType("application/plain;charset=utf-8");
                            PrintWriter writer = response.getWriter();
                            writer.write(CommunityUtil.getJSONString(403,"您还没有登录！"));
                        }else{
                            response.sendRedirect(request.getContextPath()+"/login");
                        }
                    }
                })
                .accessDeniedHandler(new AccessDeniedHandler() {  //登录了但是权限不足的处理
                    @Override
                    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException e) throws IOException, ServletException {
                        String xRequestedWith = request.getHeader("x-requested-with");
                        if(xRequestedWith.equals("XMLHttpRequest")){
                            response.setContentType("application/plain;charset=utf-8");
                            PrintWriter writer = response.getWriter();
                            writer.write(CommunityUtil.getJSONString(403,"你没有访问此功能的权限"));
                        }else{
                            response.sendRedirect(request.getContextPath()+"/denied");
                        }
                    }
                });
         //Security底层默认会拦截/logout请求进行退出处理，覆盖它默认的逻辑，才能执行我们自己的退出代码
        http.logout().logoutUrl("/securitylogout");

    }
}
