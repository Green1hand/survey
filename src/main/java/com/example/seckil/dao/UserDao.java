package com.example.seckil.dao;

import com.example.seckil.entity.User;

/**
 * @program: seckil
 * @description：TODO
 * @author: tilldawn
 * @create: 2021-09-20 17:46
 */
public interface UserDao {

    /**
     * 根据用户I的查询用户信息
     * @param id 用户Id
     * @return 用户信息
     */
    User checkedUser(Integer id);
}
