
package com.mybatisplus.example;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mybatisplus.example.entity.Role;
import com.mybatisplus.example.entity.User;
import com.mybatisplus.example.service.IMenuService;
import com.mybatisplus.example.service.IUserService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.Date;

@SpringBootApplication
@ComponentScan("com.mybatisplus")
public class ExampleApplication {

    @Resource
    private IUserService userServiceImpl;
    @Resource
    private IMenuService menuServiceImpl;


    public static void main(String[] args) {
        SpringApplication.run(ExampleApplication.class, args);
    }

    @SuppressWarnings("unchecked")
    @PostConstruct
    public void init() {
        QueryWrapper<User> wrapper = new QueryWrapper<>();

        User user1 = new User();
        user1.setUsername("admin");
        wrapper.setEntity(user1);

        User user = userServiceImpl.getOne(wrapper);
        System.out.println(user);

        Role role = new Role();
        role.setCreateTime(new Date());
        role.setName("管理员");
        role.setRemark("我是一个管理员");

        role.insert();
        System.out.println(role);
    }

    public void testColumnName() {
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        //选择的列
        wrapper.excludeColumns(User.USERNAME, User.PASSWORD, User.ID);
        //自定义select
        wrapper.select("username,password,id");

        //条件支持链式调用
        wrapper.eq("username", "admin")
                .eq(User.USERNAME, "admin");
        //排序
        wrapper.orderByAsc("id");
        //分组
        wrapper.groupBy("username");

        //分页
        userServiceImpl.page(new Page<>(1, 20), wrapper);
        //获取单个
        userServiceImpl.getOne(wrapper);
        //获取列表
        userServiceImpl.list(wrapper);
    }

    @SuppressWarnings("unchecked")
    public void testLambda() {
        QueryWrapper<User> wrapper = new QueryWrapper<>();

        //选择的列
        wrapper.excludeColumns(User.USERNAME, User.PASSWORD, User.ID);
        //自定义select

        wrapper.lambda()
                .select(User::getUsername, User::getPassword, User::getId)
                .eq(User::getUsername, "admin")
                .groupBy(User::getUsername)
                .getEntity();
        //分页
        userServiceImpl.page(new Page<>(1, 20), wrapper);
        //获取单个
        userServiceImpl.getOne(wrapper);
        //获取列表
        userServiceImpl.list(wrapper);
    }

    public void testActivityRecird(){
        Role role = new Role();
        role.setCreateTime(new Date());
        role.setName("管理员");
        role.setRemark("我是一个管理员");

        role.insert();
    }
}
