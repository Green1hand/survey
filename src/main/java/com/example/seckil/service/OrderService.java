package com.example.seckil.service;

/**
 * @author tilldawn
 */
public interface OrderService {
    /**
     * 秒杀下单方法
     * @param id 订单id
     * @return int 订单Id
     */
    int kill(Integer id);

    /**
     * 根据商品Id+用户Id生成Md5值
     * @param id 商品Id
     * @param userId 用户Id
     * @return string md5加密值
     */
    String getMd5(Integer id, Integer userId);

    /**
     * 秒杀下单方法（使用令牌桶与MD5值）
     * @param id 商品Id
     * @param userId 用户Id
     * @param md5 加密值
     * @return int 订单Id
     */
    int kill(Integer id, Integer userId, String md5);
}
