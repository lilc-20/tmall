package cn.edu.hzau.tmall.service;

import cn.edu.hzau.tmall.entity.User;
import cn.edu.hzau.tmall.util.OrderUtil;
import cn.edu.hzau.tmall.util.PageUtil;

import java.util.List;

public interface UserService {
    boolean add(User user);
    boolean update(User user);

    List<User> getList(User user, OrderUtil orderUtil, PageUtil pageUtil);
    User get(Integer user_id);
    User login(String user_name, String user_password);
    Integer getTotal(User user);
    Integer getPhone(String user_phone);

    Integer checkPwd(String user_name, String user_password);

    boolean updatePwd(String user_name, String user_password);

    Integer checkPhone(String user_name, String user_phone);

    User faceLogin(String user_name);
}
