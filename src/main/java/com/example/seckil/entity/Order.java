package com.example.seckil.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * @program: seckil
 * @description：TODO
 * @author: tilldawn
 * @create: 2021-09-19 14:02
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Accessors(chain = true)
public class Order {

    /**
     * 订单id
     */
    private Integer id;
    /**
     * 商品Id
     */
    private Integer stickId;
    /**
     * 订单名称个
     */
    private String name;
    /**
     * 创建时间
     */
    private Date createTime;
}
