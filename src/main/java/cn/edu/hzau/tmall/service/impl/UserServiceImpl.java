package cn.edu.hzau.tmall.service.impl;

import cn.edu.hzau.tmall.dao.UserMapper;
import cn.edu.hzau.tmall.entity.User;
import cn.edu.hzau.tmall.service.UserService;
import cn.edu.hzau.tmall.util.OrderUtil;
import cn.edu.hzau.tmall.util.PageUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

@Service("userService")
public class UserServiceImpl implements UserService {
    private UserMapper userMapper;
    @Resource(name = "userMapper")
    public void setUserMapper(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    @Override
    public boolean add(User user) {
        return userMapper.insertOne(user)>0;
    }

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    @Override
    public boolean update(User user) {
        return userMapper.updateOne(user)>0;
    }

    @Override
    public List<User> getList(User user, OrderUtil orderUtil, PageUtil pageUtil) {
        return userMapper.select(user,orderUtil,pageUtil);
    }

    @Override
    public User get(Integer user_id) {
        return userMapper.selectOne(user_id);
    }

    @Override
    public User login(String user_name, String user_password) {
        return userMapper.selectByLogin(user_name,user_password);
    }

    @Override
    public Integer getTotal(User user) {
        return userMapper.selectTotal(user);
    }

    @Override
    public Integer getPhone(String user_phone) {
        return userMapper.selectPhone(user_phone);
    }

    @Override
    public Integer checkPwd(String user_name, String user_password) {
        return userMapper.selectPwd(user_name, user_password);
    }

    @Override
    public boolean updatePwd(String user_name, String user_password) {
        return userMapper.updatePwd(user_name, user_password) == 1;
    }

    @Override
    public Integer checkPhone(String user_name, String user_phone) {
        return userMapper.select_Name_Phone(user_name, user_phone);
    }
}
