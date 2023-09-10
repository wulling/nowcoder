package com.example.demo.controller;

import com.example.demo.util.CommunityUtil;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;


@Controller  //该注解表示该类可以被spring容器扫描到并装配到容器中
@RequestMapping("/alpha")
public class alpc {
    @RequestMapping("/hello")
    @ResponseBody
    public String sayhello(){

        return "hello spring boot";
    }

    @RequestMapping("/http")
    public void http(HttpServletRequest request, HttpServletResponse response){
        String method = request.getMethod();
        String servletPath = request.getServletPath();
        Enumeration<String> headerNames = request.getHeaderNames(); //返回的是一个迭代器，里面存放的是key
        while(headerNames.hasMoreElements()){
            String key = headerNames.nextElement();
            String value = request.getHeader(key);
        }
        String code = request.getParameter("code");
        response.setContentType("text/html;charset=utf-8");
        try (PrintWriter writer = response.getWriter();){
            writer.write("<h1>牛客网</h1>");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    //GET请求  浏览器向服务器获取数据
    // /students?current=1&limit=20
    @RequestMapping(path = "/students",method = RequestMethod.GET)  //当请求方式为get时才可进入
    @ResponseBody
    public String getStudents(
            @RequestParam(name = "current",required = false,defaultValue = "1") int current,
            @RequestParam(name = "limit",required = false,defaultValue = "20") int limit){
        //把request中名为limit的参数赋值给后面的形参
        System.out.println(current);
        System.out.println(limit);
        return "some students";
    }
    // /student/123  直接把参数编排到路径当中
    @RequestMapping(path = "/student/{id}",method = RequestMethod.GET)  //当请求方式为get时才可进入
    @ResponseBody
    public String getStudent(@PathVariable("id") int id){ //该注解表示从路径中得到这个变量值然后赋值给形参
        //把request中名为limit的参数赋值给后面的形参
        System.out.println(id);
        return "a student";
    }

    //POST请求  浏览器向服务器提交数据
    @RequestMapping(path = "/student",method = RequestMethod.POST)
    @ResponseBody  //返回的是html就不需要加这个注解（不加这个注解默认返回html)
    public String saveStudent(String name,int age){  //形参名和表单中数据名一致就能传进来
        System.out.println(name);
        System.out.println(age);
        return "succes";
    }

    //响应html数据
    @RequestMapping(path="/teacher",method = RequestMethod.GET)
    public ModelAndView getTeacher(){
        ModelAndView mav = new ModelAndView();
        mav.addObject("name","张三");
        mav.addObject("age",30);
        mav.setViewName("/demo/view"); //设置模板的路径和名字
        return mav;

    }
    @RequestMapping(path="/school",method = RequestMethod.GET)  //这个方法较简洁，推荐
    public String getSchool(Model model){
        model.addAttribute("name","结合");
        model.addAttribute("age",11123);

        return "/demo/view";  //返回的是view的路径

    }

    //响应json数据（异步请求（访问了服务器，当前网页不会跳转）） java对象 -> json字符串  -> js对象
    @RequestMapping(path = "/emp",method = RequestMethod.GET)
    @ResponseBody
    public Map<String,Object> getemp(){
        Map<String,Object> map = new HashMap<>();
        map.put("name","张三");
        map.put("age",23);
        return map;    //dispatchServelet会自动将map对象转换为json字符串发送给浏览器
    }

    @RequestMapping(path = "/emps",method = RequestMethod.GET)
    @ResponseBody
    public List<Map<String,Object>> getemps(){
        List<Map<String,Object>> list = new ArrayList<>();
        Map<String,Object> map1 = new HashMap<>();
        map1.put("name","张三");
        map1.put("age",23);
        map1.put("hobby","抽烟");
        Map<String,Object> map2 = new HashMap<>();
        map2.put("name","李四");
        map2.put("age",22);
        map2.put("hobby","运动");
        Map<String,Object> map3 = new HashMap<>();
        map3.put("name","王五");
        map3.put("age",24);
        map3.put("hobby","跳绳");
        list.add(map1);
        list.add(map2);
        list.add(map3);
        return list;
    }

//    分布式session共享方案：
//     1、粘性session：在nginx中提供一致性哈希策略，可以保持用户ip进行hash值计算固定分配到某台服务器上，负载也比较均衡，其问题是假如有一台服务器挂了，session也丢失了。
//     2、同步session：当某一台服务器存了session后，同步到其他服务器中，其问题是同步session到其他服务器会对服务器性能产生影响，服务器之间耦合性较强。
//     3、共享session：单独搞一台服务器用来存session，其他服务器都向这台服务器获取session，其问题是这台服务器挂了，session就全部丢失。
//     4、不存储到session中而是存到redis数据库中(主流方法)：redis为内存数据库，读写效率高，并可在集群环境下做高可用。

    @RequestMapping(path="/cookie",method = RequestMethod.GET)
    @ResponseBody
    public String setcookie(HttpServletResponse response){
        Cookie cookie = new Cookie("id","1");
        cookie.setPath("/community/alpc");
        cookie.setMaxAge(60*3);
        response.addCookie(cookie);
        return "set cookie";
    }

    @RequestMapping(path="/getcookie",method = RequestMethod.GET)
    @ResponseBody
    public String getcookie(@CookieValue("id") String id){
        System.out.println(id);

        return "get cookie";
    }

    @RequestMapping(path="/setsession",method = RequestMethod.GET)
    @ResponseBody
    public String setsession(HttpSession session){
        session.setAttribute("name","刘波");
        session.setAttribute("age",24);

        return "set session";
    }

    @RequestMapping(path="/getsession",method = RequestMethod.GET)
    @ResponseBody
    public String getsession(HttpSession session){
        System.out.println(session.getAttribute("name"));
        System.out.println(session.getAttribute("age"));
        return "set session";
    }

    //ajax示例
    @RequestMapping(path="/ajax",method = RequestMethod.POST)
    @ResponseBody
    public String testAjax(String name,int age){
        System.out.println(name);
        System.out.println(age);
        return CommunityUtil.getJSONString(0,"操作成功！");
    }














}








