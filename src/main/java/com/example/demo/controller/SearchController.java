package com.example.demo.controller;

import com.example.demo.entity.DiscussPost;
import com.example.demo.entity.Page;
import com.example.demo.service.ElasticsearchService;
import com.example.demo.service.LikeService;
import com.example.demo.service.UserService;
import com.example.demo.util.CommunityConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class SearchController implements CommunityConstant {
    @Autowired
    private ElasticsearchService elasticService;
    @Autowired
    private UserService userService;
    @Autowired
    private LikeService likeService;

    //search?keyword=xxx
    @RequestMapping(path="/search",method = RequestMethod.GET)
    public String search(String keyword, Page page, Model model){
        //搜素帖子
        org.springframework.data.domain.Page<DiscussPost> searchResult =
                elasticService.searchDiscussPost(keyword, page.getCurrent() - 1, page.getLimit());
        //聚合数据
        List<Map<String,Object>> discussPosts = new ArrayList<>();
        if(searchResult!=null){
            for(DiscussPost post:searchResult){
                Map<String,Object> map = new HashMap<>();
                //帖子
                map.put("post",post);
                //作者
                map.put("user",userService.findUserById(post.getUserId()));
                //点赞数量
                map.put("likeCount",likeService.findEntityLikeCount(ENTITY_TYPE_POST,post.getId()));

                discussPosts.add(map);

            }
        }

        model.addAttribute("discussPosts",discussPosts);
        model.addAttribute("keyword",keyword);
        page.setPath("/search?keyword="+keyword);
        page.setRows(searchResult==null ? 0 : (int)searchResult.getTotalElements());

        return "/site/search";

    }
}
