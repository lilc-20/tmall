package cn.edu.hzau.tmall.dao;

import cn.edu.hzau.tmall.entity.User;
import cn.edu.hzau.tmall.util.OrderUtil;
import cn.edu.hzau.tmall.util.PageUtil;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface UserMapper {
    Integer insertOne(@Param("user") User user);
    Integer updateOne(@Param("user") User user);

    List<User> select(@Param("user") User user, @Param("orderUtil") OrderUtil orderUtil, @Param("pageUtil") PageUtil pageUtil);
    User selectOne(@Param("user_id") Integer user_id);
    User selectByLogin(@Param("user_name") String user_name, @Param("user_password") String user_password);
    Integer selectTotal(@Param("user") User user);
    Integer selectPhone(@Param("user_phone") String user_phone);

    Integer selectPwd(@Param("user_name") String user_name, @Param("user_password") String user_password);

    Integer updatePwd(@Param("user_name") String user_name, @Param("user_password") String user_password);

    Integer select_Name_Phone(@Param("user_name") String user_name, @Param("user_phone") String user_phone);
}
