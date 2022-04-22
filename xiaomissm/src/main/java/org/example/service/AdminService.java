package org.example.service;

import org.example.pojo.Admin;

public interface AdminService {

    //查询用户是否存在
    Admin login(String name,String pwd);
}
