package com.example.demo.dao;
import com.example.demo.entity.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper {

    User selectById(int id);
    User selectByName(String username);
    User selectByEmail(String email);

    int insertUser(User user);  //返回插入数据的行数

    int updateStatus(int id,int status);  //以id为条件修改状态

    int updateHeader(int id,String headerUrl);  //更新头像路径

    int updatePassword(int id,String password);

}
