package com.mybatisplus.example.service;

import com.mybatisplus.generator.bean.SysMenu;

import java.util.List;

public interface  IMenuService {
    List<SysMenu> findSysMenuByName(String name);
}
