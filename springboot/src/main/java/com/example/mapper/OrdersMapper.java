package com.example.mapper;

import com.example.entity.Orders;
import org.apache.ibatis.annotations.Select;


import java.util.List;
import java.util.Map; // 新增导入

/**
 * 操作orders相关数据接口
*/
public interface OrdersMapper {

    /**
      * 新增
    */
    int insert(Orders orders);

    /**
      * 删除
    */
    int deleteById(Integer id);

    /**
      * 修改
    */
    int updateById(Orders orders);

    /**
      * 根据ID查询
    */
    Orders selectById(Integer id);

    /**
      * 查询所有
    */
    List<Orders> selectAll(Orders orders);

    // 新增：统计购买力
    List<Map<String, Object>> selectPurchasePower();



}