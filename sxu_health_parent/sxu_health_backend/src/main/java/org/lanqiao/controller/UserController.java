package org.lanqiao.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import org.lanqiao.constant.MessageConstant;
import org.lanqiao.entity.PageResult;
import org.lanqiao.entity.QueryPageBean;
import org.lanqiao.entity.Result;
import org.lanqiao.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * 用户操作
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @Reference
    private UserService userService;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;


    //获得当前登录用户的用户名
    @RequestMapping("/getUsername")
    public Result getUsername() {
        //当Spring security完成认证后，会将当前用户信息保存到框架提供的上下文对象
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        System.out.println(user);
        if (user != null) {
            String username = user.getUsername();
            return new Result(true, MessageConstant.GET_USERNAME_SUCCESS, username);
        }
        return new Result(false, MessageConstant.GET_USERNAME_FAIL);
    }


    @RequestMapping("/loginOk")
    public Result loginOk() {
        return new Result(true, MessageConstant.LOGIN_SUCCESS);
    }

    @RequestMapping("/loginError")
    public Result loginError() {
        return new Result(false, MessageConstant.LOGIN_FAIL);
    }

    @PreAuthorize("hasAnyAuthority('USER_ADD')") // 权限校验
    @RequestMapping("/add")
    public Result add(@RequestBody org.lanqiao.pojo.User user, Integer[] roleIds) {
        try {
            user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
            userService.add(user, roleIds);
            return new Result(true, MessageConstant.ACTION_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.ACTION_FAIL);
        }
    }

    @RequestMapping("/findPage")
    public PageResult findPage(@RequestBody QueryPageBean queryPageBean) {
        try {
            PageResult pageResult = userService.pageQuery(queryPageBean);
            return pageResult;
        } catch (Exception e) {
            e.printStackTrace();
            return new PageResult(0L, null);
        }
    }

    @RequestMapping("findById")
    public Result findById(Integer id) {
        try {
            org.lanqiao.pojo.User user = userService.findById(id);
            return new Result(true, MessageConstant.ACTION_SUCCESS, user);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.ACTION_FAIL);
        }
    }

    @RequestMapping("/findRoleIdsByUserId")
    public Result findRoleIdsByUserId(Integer id) {
        try {
            List<Integer> list = userService.findRoleIdsByUserId(id);
            return new Result(true, MessageConstant.ACTION_SUCCESS, list);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.ACTION_FAIL);
        }

    }

    @PreAuthorize("hasAnyAuthority('USER_EDIT')") // 权限校验
    @RequestMapping("/edit")
    public Result edit(@RequestBody org.lanqiao.pojo.User user, Integer[] roleIds) {
        try {
            user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
            userService.edit(user, roleIds);
            return new Result(true, MessageConstant.ACTION_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.ACTION_FAIL);
        }
    }

    @PreAuthorize("hasAnyAuthority('USER_DELETE ')") // 权限校验
    @RequestMapping("delete")
    public Result delete(Integer id) {
        try {
            userService.deleteById(id);
            return new Result(true, MessageConstant.ACTION_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.ACTION_FAIL);
        }
    }

    @RequestMapping("/getMenuListByUsername")
    public Result getMenuListByUsername(String username) {
        try {
            List<Map<String, Object>> list = userService.getMenuListByUsername(username);
            System.out.println(list);
            return new Result(true, MessageConstant.ACTION_SUCCESS, list);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.ACTION_FAIL);
        }
    }


}
