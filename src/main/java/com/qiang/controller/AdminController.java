package com.qiang.controller;

import com.qiang.pojo.Admin;
import com.qiang.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private AdminService adminService;

    @RequestMapping("/login")   //name、pwd这两个参数来自于<form>标签下的<input>标签的name属性的属性值
    public String login(String name, String pwd, HttpServletRequest request){

        Admin admin = adminService.login(name, pwd);    //登录判断，调用业务逻辑层验证用户

        if(admin!=null){

            request.setAttribute("admin",admin);    //对象携带数据传到jsp页面

            return "main";

        }else {

            request.setAttribute("errmsg","用户名或密码错误");

            return "login";
        }


    }

}
