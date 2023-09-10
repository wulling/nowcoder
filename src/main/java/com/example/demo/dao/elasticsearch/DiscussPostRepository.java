package com.example.demo.dao.elasticsearch;

import com.example.demo.entity.DiscussPost;
import org.springframework.data.elasticsearch.repository.ElasticsearchCrudRepository;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

@Repository                                                    //声明接口要处理的实体类，声明主键的类型
public interface DiscussPostRepository extends ElasticsearchRepository<DiscussPost,Integer> {

}
