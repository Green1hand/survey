package com.example.seckil.service.impl;

import com.example.seckil.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.TimeUnit;

/**
 * @program: seckil
 * @description：TODO
 * @author: tilldawn
 * @create: 2021-09-19 11:20
 */
@Service
@Transactional
@Slf4j
public class UserServiceImpl  implements UserService {

    @Autowired(required = false)
    private StringRedisTemplate stringRedisTemplate;

    /**
     * 获取存放在Redis中的请求次数
     * @param userId 用户ID
     * @return 返回当前次数
     */
    @Override
    public int saveUserCount(Integer userId) {
        //根据不同用户id生成调用次数的key
        String limitKey = "LIMIT" + "_" + userId;
        //获取redis中指定key的调用次数
        String limitNum = stringRedisTemplate.opsForValue().get(limitKey);
        int limit =-1;
        if (limitNum == null) {
            //第一次调用放入redis中设置为0
            stringRedisTemplate.opsForValue().set(limitKey, "0", 3600, TimeUnit.SECONDS);
        } else {
            //不是第一次调用每次+1
            limit = Integer.parseInt(limitNum) + 1;
            stringRedisTemplate.opsForValue().set(limitKey, String.valueOf(limit), 3600, TimeUnit.SECONDS);
        }
        // 返回调用次数
        return limit;
    }

    /**
     * 判断是否达到访问次数
     * @param userId 用户ID
     * @return 是否
     */
    @Override
    public boolean getUserCount(Integer userId) {
        String limitKey = "LIMIT"+ "_" + userId;
        //跟库用户调用次数的key获取redis中调用次数
        String limitNum = stringRedisTemplate.opsForValue().get(limitKey);
        if (limitNum == null) {
            //为空直接抛弃说明key出现异常
            log.error("该用户没有访问申请验证值记录，疑似异常");
            return true;
        }
        //false代表没有超过 true代表超过
        return Integer.parseInt(limitNum) > 10;
    }
}

