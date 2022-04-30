package org.lanqiao.dao;

import org.lanqiao.pojo.OrderSetting;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface OrderSettingDao {
    public void add(OrderSetting orderSetting);
    //更新可预约人数
    public void editNumberByOrderDate(OrderSetting orderSetting);
    //根据预约日期查询
    public long findCountByOrderDate(Date orderDate);
    //根据日期范围查询预约设置信息
    public List<OrderSetting> getOrderSettingByMonth(Map map);
    // 移动端
    //根据预约日期查询预约设置信息
    public OrderSetting findByOrderDate(Date orderDate);
    //根据预约日期更新 已预约人数
    public void editReservationsByOrderDate(OrderSetting orderSetting);

}
