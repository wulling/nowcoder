package com.example.demo.controller;

import com.example.demo.annotation.LoginRequired;
import com.example.demo.entity.Comment;
import com.example.demo.entity.DiscussPost;
import com.example.demo.entity.Page;
import com.example.demo.entity.User;
import com.example.demo.service.*;
import com.example.demo.util.CommunityConstant;
import com.example.demo.util.CommunityUtil;
import com.example.demo.util.HostHolder;
import com.qiniu.util.Auth;
import com.qiniu.util.StringMap;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping(path="/user")
public class UserController implements CommunityConstant {
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Value("${community.path.upload}")
    private String uploadPath;

    @Value("${server.servlet.context-path}")
    private String contextPath;

    @Value("${community.path.domain}")
    private String domain;

    @Autowired
    private UserService userService;

    @Autowired
    private HostHolder hostHolder;

    @Autowired
    private LikeService likeService;

    @Autowired
    private FollowService followService;

    @Autowired
    private DiscussPostService discussPostService;

    @Autowired
    private CommentService commentService;

    @Value("${qiniu.key.access}")
    private String accessKey;
    @Value("${qiniu.key.secret}")
    private String secretKey;
    @Value("${qiniu.bucket.header.name}")
    private String headerBucketName;
    @Value("${qiniu.bucket.header.url}")
    private String headerBucketUrl;

    @LoginRequired
    @RequestMapping(path="/setting",method = RequestMethod.GET)
    public String getSettingPage(Model model){
        //上传文件名称
        String fileName = CommunityUtil.generateUUID();
        //设置响应消息
        StringMap policy = new StringMap();
        policy.put("returnBody",CommunityUtil.getJSONString(0));
        //生成上传凭证
        Auth auth = Auth.create(accessKey,secretKey);
        String uploadToken = auth.uploadToken(headerBucketName, fileName, 3600, policy);

        model.addAttribute("uploadToken",uploadToken);
        model.addAttribute("fileName",fileName);
        return "/site/setting";
    }

    //更新头像路径
    @RequestMapping(path="/header/url",method = RequestMethod.POST)
    @ResponseBody
    public String updateHeaderUrl(String fileName){
        if(StringUtils.isBlank(fileName)){
            return CommunityUtil.getJSONString(1,"文件名不能为空！");
        }
        String url = headerBucketUrl+"/"+fileName;
        userService.updateHeader(hostHolder.getUser().getId(),url);
        return CommunityUtil.getJSONString(0);
    }

    //废弃
    @LoginRequired
    @RequestMapping(path="/upload",method = RequestMethod.POST)
    public String uploadHeader(MultipartFile headerimage, Model model){  //用MultipartFile来接收页面上传入的文件,如果页面传的是一个文件，则声明一个就行，否则可以声明成数组
        if(headerimage==null){
            model.addAttribute("error","您还没有选择图片!");
            return "/site/setting";
        }
        String filename = headerimage.getOriginalFilename();
        String suffix = filename.substring(filename.lastIndexOf("."));
        if(StringUtils.isBlank(suffix)){
            model.addAttribute("error","文件格式不正确!");
            return "/site/setting";
        }
        //生成随机文件名
        filename = CommunityUtil.generateUUID()+suffix;
        //确定文件存放路径
        File dest = new File(uploadPath+"/"+filename);
        try {
            //存储文件
            headerimage.transferTo(dest);  //把当前文件内容写入dest中
        } catch (IOException e) {
            logger.error("上传文件失败:"+e.getMessage());
            throw new RuntimeException("上传文件失败，服务器发生异常！"+e);
        }

        //更新当前用户的头像的路径（web访问路径）
        //http://localhost:8080/community/user/header/xxx.png
        User user = hostHolder.getUser();
        String headUrl = domain+contextPath+"/user/header/"+filename;
        userService.updateHeader(user.getId(),headUrl);

        return "redirect:/index";
    }

    //废弃
    @RequestMapping(path="/header/{filename}",method = RequestMethod.GET)
    public void getHeader(@PathVariable("filename") String filename, HttpServletResponse response){
        //服务器存放路径
        filename = uploadPath+"/"+filename;
        //文件后缀
        String suffix = filename.substring(filename.lastIndexOf(".")+1);
        //响应图片
        response.setContentType("image/"+suffix);
        try (
                FileInputStream fis = new FileInputStream(filename);
                ServletOutputStream os = response.getOutputStream();
                )
        {

            byte[] buffer = new byte[1024];
            int b=0;
            while((b=fis.read(buffer))!=-1){
                os.write(buffer,0,b);
            }
        } catch (IOException e) {
            logger.error("读取头像失败:"+e.getMessage());
        }

    }

    @LoginRequired
    @RequestMapping(path="/modifypassword", method = RequestMethod.POST)
    public String modifyPassword(String oldpassword,String newpassword ,Model model){
        if(oldpassword==null || newpassword==null ) {
            model.addAttribute("passwordMsg", "密码不能为空！");
            return "/site/setting";
        }
        if(oldpassword.length()<8 || newpassword.length()<8 ){
            model.addAttribute("newpasswordMsg", "密码不能小于8位！");
            return "/site/setting";
        }
        User user = hostHolder.getUser();
        oldpassword  = CommunityUtil.md5(oldpassword + user.getSalt());
        if(!user.getPassword().equals(oldpassword)){
            model.addAttribute("oldpasswordMsg", "密码错误！");
            return "/site/setting";
        }

        newpassword = CommunityUtil.md5(newpassword+user.getSalt());
        userService.updatePassword(user.getId(),newpassword);
//        model.addAttribute("msg","密码修改成功，请重新登陆！");
//        model.addAttribute("target","/login");
        return "redirect:/logout";
    }

    //个人主页
    @RequestMapping(path="/profile/{userId}",method = RequestMethod.GET)
    public String getProfilePage(@PathVariable("userId") int userId,Model model){
        User user = userService.findUserById(userId);
        if(user==null){
            throw new RuntimeException("该用户不存在！");
        }
        //用户
        model.addAttribute("user",user);
        //用户的点赞数量
        int likeCount = likeService.findUserLikeCount(userId);
        model.addAttribute("likeCount",likeCount);

        //关注数量
        long followeeCount = followService.findFolloweeCount(userId, ENTITY_TYPE_USER);
        model.addAttribute("followeeCount",followeeCount);
        //粉丝数量
        long followerCount = followService.findFollowerCount(ENTITY_TYPE_USER, userId);
        model.addAttribute("followerCount",followerCount);
        //是否已关注
        boolean hasFollowed = false;
        if(hostHolder.getUser()!=null){
            hasFollowed = followService.hasFollowed(hostHolder.getUser().getId(), ENTITY_TYPE_USER, userId);
        }
        model.addAttribute("hasFollowed",hasFollowed);

        return "/site/profile";

    }

    @RequestMapping(path="/mypost/{userId}",method = RequestMethod.GET)
    public String getMyPostPage(@PathVariable("userId") int userId, Page page, Model model){
        User user = userService.findUserById(userId);
        if(user==null){
            throw new RuntimeException("该用户不存在！");
        }

        model.addAttribute("user",user);

        //帖子数量
        int discussPostRows = discussPostService.findDiscussPostRows(userId);
        model.addAttribute("discussPostRows",discussPostRows);

        page.setLimit(5);
        page.setPath("/user/mypost/"+userId);
        page.setRows(discussPostRows);
        List<DiscussPost> discussPosts = discussPostService.findDiscussPosts(userId, page.getOffset(), page.getLimit(),0);
        List<Map<String,Object>> discussPostList = new ArrayList<>();
        if(discussPosts!=null){
            for(DiscussPost post:discussPosts){
                Map<String,Object> map = new HashMap<>();
                map.put("post",post);
                //点赞数量
                long likeCount = likeService.findEntityLikeCount(ENTITY_TYPE_POST,post.getId());
                map.put("likeCount",likeCount);
                discussPostList.add(map);
            }
        }

        model.addAttribute("discussPostList",discussPostList);
        return "/site/my-post";
    }

    @RequestMapping(path="/myreply/{userId}",method = RequestMethod.GET)
    public String getMyreplayPage(@PathVariable("userId") int userId,Page page,Model model){
        User user = userService.findUserById(userId);
        if(user==null){
            throw new RuntimeException("该用户不存在！");
        }
        model.addAttribute("user",user);
        int userCommentCount = commentService.findUserCommentCount(userId);
        model.addAttribute("userCommentCount",userCommentCount);
        page.setLimit(5);
        page.setRows(userCommentCount);
        page.setPath("/user/myreply/"+userId);
        List<Comment> comments = commentService.findCommentsByUser(userId, page.getOffset(), page.getLimit());
        List<Map<String, Object>> commentVOList = new ArrayList<>();
        if(comments!=null){
            for(Comment comment:comments){
                Map<String, Object> map = new HashMap<>();
                map.put("comment",comment);
                DiscussPost post = discussPostService.findDiscussPostById(comment.getEntityId());
                map.put("post",post);
                commentVOList.add(map);
            }
        }

        model.addAttribute("commentVOList",commentVOList);
        return "site/my-reply";

    }


}
