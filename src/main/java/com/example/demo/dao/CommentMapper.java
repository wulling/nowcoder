package com.example.demo.dao;

import org.apache.ibatis.annotations.Mapper;
import com.example.demo.entity.Comment;

import java.util.List;

@Mapper
public interface CommentMapper {
    List<Comment> selectCommentByEntity(int entityType,int entityId,int offset,int limit);

    int selectCountByEntity(int entityType,int entityId);

    int insertComment(Comment comment);

    Comment selectCommentById(int id);

    List<Comment> selectCommentsByUser(int userId,int offset,int limit);

    int selectCountByUser(int userId);

}
