package org.example.service.impl;

import org.example.mapper.AdminMapper;
import org.example.pojo.Admin;
import org.example.pojo.AdminExample;
import org.example.service.AdminService;
import org.example.utils.MD5Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminServiceImpl implements AdminService {

    //在业务逻辑层，一定会有数据访问层的对象
    //@Autowired，代替其实现类
    @Autowired
    AdminMapper adminMapper;

    @Override
    public Admin login(String name, String pwd) {

        //封装条件
        AdminExample example = new AdminExample();

        /**
         * 如何添加条件
         * select * from admin where a_name = 'admin'
         * admin == name
         */
        //添加用户名a_name条件
        example.createCriteria().andANameEqualTo(name);

        List<Admin> list = adminMapper.selectByExample(example);

        if (list.size() > 0) {
            Admin admin = list.get(0);

            //pwd需要加密后，再和数据库查询出的结果比较
            String mipwd = MD5Util.getMD5(pwd);

            if (mipwd.equals(admin.getaPass())) {
                return admin;
            }
        }
        return null;
    }
}












