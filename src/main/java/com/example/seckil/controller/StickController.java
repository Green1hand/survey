package com.example.seckil.controller;

import com.example.seckil.service.OrderService;
import com.example.seckil.service.UserService;
import com.google.common.util.concurrent.RateLimiter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.TimeUnit;

/**
 * @program: seckil
 * @description：TODO
 * @author: tilldawn
 * @create: 2021-09-19 11:06
 */
@RestController
@RequestMapping("/stick")
@Slf4j
public class StickController {

    @Autowired
    private OrderService orderService;
    @Autowired
    private UserService userService;

    /**
     * 创建令牌桶实例
     */
    private final RateLimiter rateLimiter = RateLimiter.create(20);

    /**
     * 演示令牌桶实现接口限流
     * @param id 商品Id
     * @return String
     */
    @GetMapping("/sale")
    public String sale(Integer id) {

        // 若没有取到token令牌，则直到取到为止
        log.info("等待时间：" + rateLimiter.acquire());
        // 设置一个等待时间，若在等待时间内无法获取令牌则放弃，反之通过
        int time = 5;
        if (rateLimiter.tryAcquire(time, TimeUnit.SECONDS)) {
            System.out.println("当前请求已被限制！");
            return "";
        }
        return "";
    }

    /**
     * 根据商品Id+用户Id生成Md5值
     * @param id 商品Id
     * @param userId 用户Id
     * @return String md5
     */
    @GetMapping("/md5")
    public String gerMd5(Integer id, Integer userId) {

        String Md5;
        try {
            Md5 = orderService.getMd5(id, userId);
        } catch (Exception e) {
            e.printStackTrace();
            return "获取Md5失败：" + e.getMessage();
        }
        return "获取MD5成功！" + Md5;
    }

    /**
     * 开发秒杀方法
     * @return String
     */
    @GetMapping("/kill")
    public String kill(Integer id) {

        System.out.println("秒杀商品的id = " + id);
        try {
            // 根据秒杀商品的id 去调用秒杀业务
            int orderId = orderService.kill(id);
            return "秒杀成功，订单id为：" + String.valueOf(orderId);
        } catch (Exception e) {
            e.printStackTrace();
            return e.getMessage();
        }
    }

    /**
     * 开发秒杀方法(使用令牌桶)
     * @return String
     */
    @GetMapping("/killToken")
    public String killToken(Integer id) {

        System.out.println("秒杀商品的id = " + id);
        // 加入令牌桶的限流方法
        int time = 3;
        if (!rateLimiter.tryAcquire(time, TimeUnit.SECONDS)) {
            log.info("当前抢购人数过多，请稍后再试！");
            return "当前抢购人数过多，请稍后再试！";
        }
        try {
            // 根据秒杀商品的id 去调用秒杀业务
            int orderId = orderService.kill(id);
            return "秒杀成功，订单id为：" + String.valueOf(orderId);
        } catch (Exception e) {
            e.printStackTrace();
            return e.getMessage();
        }
    }

    /**
     * 开发秒杀方法(使用令牌桶与MD5值)
     * @return String
     */
    @GetMapping("/killTokenMd5")
    public String killTokenMd5(Integer id, Integer userId, String md5) {

        System.out.println("秒杀商品的id = " + id);
        // 加入令牌桶的限流方法
        int time = 3;
        if (!rateLimiter.tryAcquire(time, TimeUnit.SECONDS)) {
            log.info("当前抢购人数过多，请稍后再试！");
            return "当前抢购人数过多，请稍后再试！";
        }
        try {
            // 根据秒杀商品的id 去调用秒杀业务
            int orderId = orderService.kill(id, userId, md5);
            return "秒杀成功，订单id为：" + String.valueOf(orderId);
        } catch (Exception e) {
            e.printStackTrace();
            return e.getMessage();
        }
    }

    /**
     * 开发秒杀方法(使用令牌桶与MD5值)
     * @return String
     */
    @GetMapping("/killTokenMd5Emit")
    public String killTokenMd5Emit(Integer id, Integer userId, String md5) {

        System.out.println("秒杀商品的id = " + id);
        // 加入令牌桶的限流方法
        int time = 3;
        if (!rateLimiter.tryAcquire(time, TimeUnit.SECONDS)) {
            log.info("当前抢购人数过多，请稍后再试！");
            return "当前抢购人数过多，请稍后再试！";
        }
        try {
            //加入单用户限制调用频率
            int count = userService.saveUserCount(userId);
            log.info("用户截至该次的访问次数为: [{}]", count);
            boolean isBanned = userService.getUserCount(userId);
            if (isBanned) {
                log.info("购买失败,超过频率限制!");
                return "购买失败，超过频率限制!";
            }
            // 根据秒杀商品的id 去调用秒杀业务
            int orderId = orderService.kill(id, userId, md5);
            return "秒杀成功，订单id为：" + orderId;
        } catch (Exception e) {
            e.printStackTrace();
            return e.getMessage();
        }
    }
}
