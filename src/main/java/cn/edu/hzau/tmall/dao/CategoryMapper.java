package cn.edu.hzau.tmall.dao;

import cn.edu.hzau.tmall.entity.Category;
import cn.edu.hzau.tmall.util.PageUtil;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface CategoryMapper {
    Integer insertOne(@Param("category") Category category);
    Integer updateOne(@Param("category") Category category);

    List<Category> select(@Param("category_name") String category_name, @Param("pageUtil") PageUtil pageUtil);
    Category selectOne(@Param("category_id") Integer category_id);
    Integer selectTotal(@Param("category_name") String category_name);
}