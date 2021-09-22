package com.example.seckil.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * @program: seckil
 * @description：TODO
 * @author: tilldawn
 * @create: 2021-09-19 11:32
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Accessors(chain = true)
public class Stick {

    /**
     * 商品id
     */
    private Integer id;
    /**
     * 商品名称
     */
    private String name;
    /**
     * 商品初始数量
     */
    private Integer count;
    /**
     * 商品已售出数量
     */
    private Integer sale;
    /**
     * 商品版本号
     */
    private Integer version;
}
