package com.mybatisplus.example.service.impl;

import com.mybatisplus.example.dao.RoleMapper;
import com.mybatisplus.example.entity.Role;
import com.mybatisplus.example.service.IRoleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 角色 服务实现类
 * </p>
 *
 * @author K神带你飞
 * @since 2018-08-15
 */
@Service
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements IRoleService {

}
