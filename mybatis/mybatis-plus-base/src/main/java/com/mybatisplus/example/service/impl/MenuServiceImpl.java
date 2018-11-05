package com.mybatisplus.example.service.impl;

import com.mybatisplus.example.service.IMenuService;
import com.mybatisplus.generator.bean.SysMenu;
import com.mybatisplus.generator.bean.SysMenuExample;
import com.mybatisplus.generator.dao.SysMenuMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class MenuServiceImpl implements IMenuService {

    @Resource
    private SysMenuMapper sysMenuMapper;

    @Override
    public List<SysMenu> findSysMenuByName(String name){
        SysMenuExample example = new SysMenuExample();
        //去除重复
        example.setDistinct(true);
        //排序
        example.setOrderByClause("id");
        //条件
        example.createCriteria()
                .andNameEqualTo(name)
                .andIconIsNotNull()
                .andIdBetween(1L,2L);
        return sysMenuMapper.selectByExample(example);
    }
}
