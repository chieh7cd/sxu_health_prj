package org.lanqiao.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import org.lanqiao.constant.MessageConstant;
import org.lanqiao.constant.RedisConstant;
import org.lanqiao.entity.PageResult;
import org.lanqiao.entity.QueryPageBean;
import org.lanqiao.entity.Result;
import org.lanqiao.pojo.Setmeal;
import org.lanqiao.service.SetmealService;
import org.lanqiao.utils.AliyunUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import redis.clients.jedis.JedisPool;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

/**
 * 体检套餐管理
 */
@RestController
@RequestMapping("/setmeal")
public class SetmealController {

    //使用JedisPool操作Redis服务
    @Autowired
    private JedisPool jedisPool;


    // 文件上传
    @RequestMapping("/upload")
    public Result upload(@RequestParam("imgFile") MultipartFile imgFile) {
        System.out.println(imgFile);
        String originalFilename = imgFile.getOriginalFilename();// 原始文件名  3hahg4lkao4hfk3h2fafp6aoj.jpg
        int index = originalFilename.lastIndexOf(".");
        String extention = originalFilename.substring(index); // .jpg
        String fileName = UUID.randomUUID().toString() + extention; // 随机字符串.jpg
        try {
            // 将文件上传到阿里云服务器
            AliyunUtils.upload2Aliyun(imgFile.getBytes(), fileName);
            // 将上传图片名称存入redis，基于redis的Set集合存储
            jedisPool.getResource().sadd(RedisConstant.SETMEAL_PIC_RESOURCES, fileName);
        } catch (IOException e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.PIC_UPLOAD_FAIL);
        }
        return new Result(true, MessageConstant.PIC_UPLOAD_SUCCESS, fileName);
    }

    @Reference
    private SetmealService setmealService;

    //新增套餐
    @RequestMapping("/add")
    public Result add(@RequestBody Setmeal setmeal, Integer[] checkgroupIds) {
        try {
            setmealService.add(setmeal, checkgroupIds);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.ADD_SETMEAL_FAIL);
        }
        return new Result(true, MessageConstant.ADD_SETMEAL_SUCCESS);
    }

    //分页查询
    @RequestMapping("/findPage")
    public PageResult findPage(@RequestBody QueryPageBean queryPageBean) {
        return setmealService.pageQuery(queryPageBean);
    }

    //根据套餐ID查询检查组IDS
    @RequestMapping("/findIdInMiddleTable")
    public Result findIdInMiddleTable(String setMealId) {
        try {
            List<String> checkgroupIds = setmealService.findIdInMiddleTable(setMealId);
            return new Result(true, MessageConstant.QUERY_CHECKGROUP_SUCCESS, checkgroupIds);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.QUERY_CHECKGROUP_FAIL);
        }
    }

    // 编辑套餐
    @RequestMapping("/edit")
    public Result edit(@RequestBody Setmeal setmeal, Integer[] checkgroupIds, String tempImgId) {
        try {
            setmealService.edit(setmeal, checkgroupIds, tempImgId);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.EDIT_SETMEAL_FAIL);
        }
        return new Result(true, MessageConstant.EDIT_SETMEAL_SUCCESS);
    }

    // 删除套餐
    @PreAuthorize("hasAnyAuthority('SETMEAL_DELETE')") // 权限校验
    @RequestMapping("/delete")
    public Result delete(Integer setmealId, String imgId) {
        try {
            setmealService.delete(setmealId, imgId);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.DELETE_SETMEAL_FAIL);
        }
        return new Result(true, MessageConstant.DELETE_SETMEAL_SUCCESS);
    }


}
