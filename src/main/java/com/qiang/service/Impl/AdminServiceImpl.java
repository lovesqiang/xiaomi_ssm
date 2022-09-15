package com.qiang.service.Impl;

import com.qiang.mapper.AdminMapper;
import com.qiang.pojo.Admin;
import com.qiang.pojo.AdminExample;
import com.qiang.service.AdminService;
import com.qiang.utils.MD5Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminServiceImpl implements AdminService {

    @Autowired
    private AdminMapper adminMapper;

    @Override
    public Admin login(String name, String pwd) {

        //如果有条件，则一定要创建AdminExample的对象用来封装条件
        AdminExample example = new AdminExample();
        example.createCriteria().andANameEqualTo(name);     //追加条件where a_name = name

        //select * from admin where a_name = name;
        List<Admin> list = adminMapper.selectByExample(example);

        if(list.size()>0){

            Admin admin = list.get(0);  //获取admin对象

            String mipwd = MD5Util.getMD5(pwd);   //将界面层传过来的密码使用MD5工具变成密文

            if(mipwd.equals(admin.getaPass())){    //判断用户密码和数据库存储的密码是否一致
                return admin;
            }
        }

        return null;
    }
}
