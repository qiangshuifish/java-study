package com.mybatisplus.example.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mybatisplus.example.dao.UserMapper;
import com.mybatisplus.example.entity.User;
import com.mybatisplus.example.service.IUserService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 系统用户 服务实现类
 * </p>
 *
 * @author K神带你飞
 * @since 2018-08-15
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {

}

