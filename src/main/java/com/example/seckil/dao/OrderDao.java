package com.example.seckil.dao;

import com.example.seckil.entity.Order;

/**
 * @program: seckil
 * @description：TODO
 * @author: tilldawn
 * @create: 2021-09-19 14:06
 */
public interface OrderDao {

    /**
     * 创建订单信息
     * @param order 订单信息
     */
    void createOrder(Order order);
}
