package org.lanqiao.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import org.lanqiao.constant.MessageConstant;
import org.lanqiao.constant.RedisMessageConstant;
import org.lanqiao.entity.Result;
import org.lanqiao.pojo.Order;
import org.lanqiao.service.OrderService;
import org.lanqiao.utils.SMSUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.JedisPool;

import java.util.Map;

/**
 * 体检预约处理
 */
@RestController
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private JedisPool jedisPool;

    @Reference
    private OrderService orderService;

    // 在线体检预约
    @RequestMapping("/submit")
    public Result submit(@RequestBody Map map) {
        String telephone = (String) map.get("telephone");
        // 从redis中获取保存的验证码
        String validateCodeInRedis = jedisPool.getResource().get(telephone + RedisMessageConstant.SENDTYPE_ORDER);
        String validateCode = (String) map.get("validateCode");
        // 将用户输入的验证码和redis中保存的验证码进比对
        if (validateCodeInRedis != null && validateCode != null && validateCode.equals(validateCodeInRedis)) {
            // 如果比对成功，调用服务完成预约业务处理
            map.put("orderType", Order.ORDERTYPE_WEIXIN); // 设置预约类型(微信渔业、电话预约)
            Result result = new Result(false, MessageConstant.SELECTED_DATE_CANNOT_ORDER);
            try {
                result = orderService.order(map);  // 通过Dubbo远程调用服务实现在线预约业务处理，可能会发成异常
            } catch (Exception e) {
                e.printStackTrace();
                return result;
            }
            if (result.isFlag()) {
                // 预约成功，可以为用户发送短信，提示预约成功
                try {
//                     预约成功，您预约的口令是666666，        短信成功通知发不过去，只能发送验证码形式
                    SMSUtils.sendShortMessage(SMSUtils.VALIDATE_CODE, telephone, (String) map.get("orderDate"));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            return result;
        } else {
            // 如果比对不成功，返回结果给页面
            return new Result(false, MessageConstant.VALIDATECODE_ERROR);
        }
    }

    // 根据预约ID查询预约相关信息
    @RequestMapping("/findById")
    public Result findById(Integer id) {
        try {
            Map map = orderService.findById(id);
            return new Result(true, MessageConstant.QUERY_ORDER_SUCCESS, map);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.QUERY_ORDER_FAIL);
        }
    }




}
