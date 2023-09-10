package com.example.demo.controller;

import com.example.demo.entity.DiscussPost;
import com.example.demo.entity.Page;
import com.example.demo.entity.User;
import com.example.demo.service.DiscussPostService;
import com.example.demo.service.LikeService;
import com.example.demo.service.UserService;
import com.example.demo.util.CommunityConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class HomeController implements CommunityConstant {
    @Autowired
    private DiscussPostService discussPostService;

    @Autowired
    private UserService userService;

    @Autowired
    private LikeService likeService;

    @RequestMapping(path="/",method = RequestMethod.GET)
    public String root(){
        return "forward:/index";
    }

    @RequestMapping(path="/index",method = RequestMethod.GET)
    public String getIndexPage(Model model, Page page,
                               @RequestParam(name="orderMode",defaultValue = "0") int orderMode){
        //所有的实体类会自动注入到model中
        //方法调用前，springMVC会自动实例化Model和Page,并将Page注入Model,所以在thymeleaf中可以直接访问Page对象中的数据
        page.setRows(discussPostService.findDiscussPostRows(0));
        page.setPath("/index?orderMode="+orderMode);
        //page的当前页码等等参数页面会传进来，page需要返回给页面的值也是算法自动算出来了
        List<DiscussPost> list = discussPostService.findDiscussPosts(0, page.getOffset(), page.getLimit(),orderMode);
        List<Map<String,Object>> discussposts = new ArrayList<>();
        if(list != null){
            for(DiscussPost post:list){
                Map<String,Object> map = new HashMap<>();
                User user = userService.findUserById(post.getUserId());
                map.put("post",post);
                map.put("user",user);

                long likeCount = likeService.findEntityLikeCount(ENTITY_TYPE_POST, post.getId());
                map.put("likeCount",likeCount);
                discussposts.add(map);
            }
        }

        model.addAttribute("discussPosts",discussposts);
        model.addAttribute("orderMode",orderMode);
        return "index";
    }

    //在templates下名为error下的两个html文件，以状态码命名，spring boot会在报错时自动跳转到对应的页面去
    //在服务器报错时如果我们不只想跳转页面，还想记录日志，并且针对异步请求的情况返回的不是页面而是json字符串，此时我们就要定义一个控制器通知组件
    //在该组件中加上我们想要的逻辑

    @RequestMapping(path="/error",method = RequestMethod.GET)
    public String getErroePage(){
        return "/error/500";
    }

    @RequestMapping(path = "/denied", method = RequestMethod.GET)
    public String getDeniedPage() {
        return "/error/404";
    }
}
