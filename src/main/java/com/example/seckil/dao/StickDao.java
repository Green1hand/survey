package com.example.seckil.dao;

import com.example.seckil.entity.Stick;

/**
 * @program: seckil
 * @description：TODO
 * @author: tilldawn
 * @create: 2021-09-19 11:31
 */
public interface StickDao {

    /**
     * 根据商品id查询商品信息
     * @param id 商品id
     * @return Stick
     */
    Stick checkStick(Integer id);

    /**
     * 更新商品信息
     * @param stick 商品信息
     * @return int 更新结果
     */
    int updateStick(Stick stick);
}
