package org.lanqiao.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import org.lanqiao.constant.MessageConstant;
import org.lanqiao.constant.RedisMessageConstant;
import org.lanqiao.entity.Result;
import org.lanqiao.pojo.Member;
import org.lanqiao.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.JedisPool;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.Map;

/**
 * 处理用户相关操作
 */
@RestController
@RequestMapping("member")
public class MemberController {

    @Autowired
    private JedisPool jedisPool;

    @Reference
    private MemberService memberService;

    // 手机号快速登录
    @RequestMapping("/login")
    public Result login(HttpServletResponse response, @RequestBody Map map) {
        String telephone = (String) map.get("telephone");
        String validateCode = (String) map.get("validateCode");

//        Member member1 = null;

        // 从redis中获取保存的验证码
        String validateCodeInRedis = jedisPool.getResource().get(telephone + RedisMessageConstant.SENDTYPE_LOGIN);
        if (validateCodeInRedis != null && validateCode != null && validateCode.equals(validateCodeInRedis)) {
            // 验证码输入正确
            // 判断当前用户是否是会员(查询会员表来确定)
            Member member = memberService.findByTelephone(telephone);

            if (member == null) {
                // 不是会员，自动完成注册(自动将用户信息保存到会员表)
                member = new Member();
                member.setRegTime(new Date());
                member.setPhoneNumber(telephone);
//                15730304040
                memberService.add(member);
            }
            // 向客户端浏览器写入Cookie，内容为手机号
            Cookie cookie = new Cookie("login_member_telephone", telephone);
            cookie.setPath("/");  //  / 代表项目下的所有请求都会带cookie过来
            cookie.setMaxAge(60*60*24*30);
            response.addCookie(cookie);
            // 将会员信息保存到Redis
            String json = JSON.toJSON(member).toString();
            jedisPool.getResource().setex(telephone, 60*30, json);
            return new Result(true, MessageConstant.LOGIN_SUCCESS);
        } else {
            // 验证码输入错误
            return new Result(false, MessageConstant.VALIDATECODE_ERROR);
        }
    }

    // 完善个人信息
    @RequestMapping("/submitInfo")
    public Result submitInfo(@RequestBody Member member) {
        try {
            memberService.updateMemberByPhone(member);
            return new Result(true, "更新用户信息成功");
        } catch (Exception e){
            return new Result(false, "更新用户信息失败");
        }
    }



}
