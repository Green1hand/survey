package com.example.seckil.service;

/**
 * @author tilldawn
 */
public interface UserService {
    /**
     * 向redis中写入用户访问次数
     * @param userId 用户ID
     * @return 访问次数
     */
    int saveUserCount(Integer userId);

    /**
     * 判断单位时间调用次数
     * @param userId 用户ID
     * @return 是否
     */
    boolean getUserCount(Integer userId);
}
