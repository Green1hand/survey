package com.example.seckil.service.impl;

import com.example.seckil.dao.OrderDao;
import com.example.seckil.dao.StickDao;
import com.example.seckil.dao.UserDao;
import com.example.seckil.entity.Order;
import com.example.seckil.entity.Stick;
import com.example.seckil.entity.User;
import com.example.seckil.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;

import java.util.Date;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * @program: seckil
 * @description：TODO
 * @author: tilldawn
 * @create: 2021-09-19 11:20
 */
@Service
@Slf4j
@Transactional
public class OrderServiceImpl implements OrderService {

    @Autowired(required = false)
    private StickDao stickDao;

    @Autowired(required = false)
    private OrderDao orderDao;

    @Autowired(required = false)
    private UserDao userDao;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

//    定义存放在redis中数据的key开头
//    private final static String STICK_NAME = "kill";

    /**
     * 秒杀
     * @param id 订单id
     * @return int
     */
    @Override
    public int kill(Integer id) {

        // 校验请求时效（redis中）
//        if (!Boolean.TRUE.equals(stringRedisTemplate.hasKey(STICK_NAME + id))) {
//            throw new RuntimeException("当前商品抢购活动已结束！");
//        }
        // 校验库存
        Stick stick = checkStick(id);
        // 扣除库存
        updateSale(stick);
        // 创建订单
        return createOrder(stick);
    }

    /**
     * 秒杀下单方法（使用令牌桶与MD5值）
     *
     * @param id     商品Id
     * @param userId 用户Id
     * @param md5    加密值
     * @return int 订单Id
     */
    @Override
    public int kill(Integer id, Integer userId, String md5) {

        // 校验请求时效（redis中）
//        if (!Boolean.TRUE.equals(stringRedisTemplate.hasKey(STICK_NAME + id))) {
//            throw new RuntimeException("当前商品抢购活动已结束！");
//        }
        // 验证签名
        String hashKey = "KEY_" + userId + "_" + id;
        if (Objects.equals(stringRedisTemplate.opsForValue().get(hashKey), null)) {
            throw new RuntimeException("当前请求数据不合法！");
        }
        if (!Objects.equals(stringRedisTemplate.opsForValue().get(hashKey), md5)) {
            throw new RuntimeException("当前请求数据不合法！");
        }
        // 校验库存
        Stick stick = checkStick(id);
        // 扣除库存
        updateSale(stick);
        // 创建订单
        return createOrder(stick);
    }

    /**
     * 根据商品Id+用户Id生成Md5值
     * @param id 商品Id
     * @param userId 用户Id
     * @return String md5
     */
    @Override
    public String getMd5(Integer id, Integer userId) {

        // 验证id信息存在
        User user = userDao.checkedUser(userId);
        if (user==null) {
            throw new RuntimeException("用户信息不存在！");
        }
        log.info("用户信息：[{}]", user);
        // 验证userId信息存在
        Stick stick = stickDao.checkStick(id);
        if (stick==null) {
            throw new RuntimeException("商品信息不存在！");
        }
        log.info("用户信息：[{}]", stick);
        // 生成Md5放入redis中
        String hashKey = "KEY_" + userId + "_" + id;
        String md5Key = DigestUtils.md5DigestAsHex((userId + id + "!ms!").getBytes());
        stringRedisTemplate.opsForValue().set(hashKey, md5Key, 360, TimeUnit.SECONDS);
        log.info("Redis写入：[{}][{}]", hashKey, md5Key);
        return md5Key;
    }

    /**
     * 校验库存
     * @param id 商品id
     * @return Stick 商品信息
     */
    private Stick checkStick(Integer id) {
        // 获取商品信息
        Stick stick = stickDao.checkStick(id);
        // 判断商品是否还有存货
        if (stick.getSale().equals(stick.getCount())) {
            throw new RuntimeException("库存不足！！！");
        }
        return stick;
    }

    /**
     * 扣除库存
     * @param stick 商品信息
     */
    private void updateSale(Stick stick) {
        // 扣除库存(从sql层面修改库存---使用版本号控制超卖)
        /*stick.setSale(stick.getSale() + 1);*/
        int i = stickDao.updateStick(stick);
        if (i == 0) {
            throw new RuntimeException("抢购失败！请稍后重试！");
        }
    }

    /**
     * 创建订单
     * @param stick 商品信息
     * @return 订单ID
     */
    private Integer createOrder(Stick stick) {

        Order order = new Order();
        order.setStickId(stick.getId()).setName(stick.getName()).setCreateTime(new Date());
        orderDao.createOrder(order);
        return order.getId();
    }
}
