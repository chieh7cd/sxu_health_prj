package org.lanqiao.controller;


import com.aliyuncs.exceptions.ClientException;
import org.lanqiao.constant.MessageConstant;
import org.lanqiao.constant.RedisMessageConstant;
import org.lanqiao.entity.Result;
import org.lanqiao.utils.SMSUtils;
import org.lanqiao.utils.ValidateCodeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.JedisPool;

/**
 * 验证码操作
 */
@RestController
@RequestMapping("/validateCode")
public class ValidateCodeController {

    @Autowired
    private JedisPool jedisPool;

    // 用户在线体检预约发送验证码
    @RequestMapping("/send4Order")
    public Result send4Order(String telephone) {
        // 随机生成一个4位的数字验证码
        Integer validateCode = ValidateCodeUtils.generateValidateCode(4);

        // 暂时测试, 人工发送
        System.out.println("111111111111111111111111::::::" + validateCode);

        // 给用户发送验证码
        try {
            SMSUtils.sendShortMessage(SMSUtils.VALIDATE_CODE, telephone, validateCode.toString());
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.SEND_VALIDATECODE_FAIL);
        }
        // 将验证码保存到redis(5分钟)  15712341234001  15712341234002  15712341234003
        jedisPool.getResource().setex(telephone + RedisMessageConstant.SENDTYPE_ORDER, 300, validateCode.toString());
        return new Result(true, MessageConstant.SEND_VALIDATECODE_SUCCESS);
    }

    // 用户手机快速登录时发送验证码
    @RequestMapping("/send4Login")
    public Result send4Login(String telephone) {
        // 随机生成一个6位的数字验证码
        Integer validateCode = ValidateCodeUtils.generateValidateCode(6);

        // 暂时测试, 人工发送
        System.out.println("222222222222222222222222222::::::" + validateCode);

        // 给用户发送验证码
        try {
            SMSUtils.sendShortMessage(SMSUtils.VALIDATE_CODE, telephone, validateCode.toString());
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.SEND_VALIDATECODE_FAIL);
        }
        // 将验证码保存到redis(5分钟)  15712341234001  15712341234002  15712341234003
        jedisPool.getResource().setex(telephone + RedisMessageConstant.SENDTYPE_LOGIN, 300, validateCode.toString());
        return new Result(true, MessageConstant.SEND_VALIDATECODE_SUCCESS);
    }

}
