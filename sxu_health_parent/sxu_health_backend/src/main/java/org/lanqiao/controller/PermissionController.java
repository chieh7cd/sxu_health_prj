package org.lanqiao.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import org.lanqiao.constant.MessageConstant;
import org.lanqiao.entity.PageResult;
import org.lanqiao.entity.QueryPageBean;
import org.lanqiao.entity.Result;
import org.lanqiao.pojo.Permission;
import org.lanqiao.service.PermissionService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/permission")
public class PermissionController {

    @Reference
    private PermissionService permissionService;

    @PreAuthorize("hasAnyAuthority('PERMISSION_ADD')") // 权限校验
    @RequestMapping("/add")
    public Result add(@RequestBody Permission permission){
        System.out.println(permission);
        try{
            permissionService.add(permission);
            return new Result(true,MessageConstant.ACTION_SUCCESS);
        }catch (Exception e){
            e.printStackTrace();
            return new Result(false,MessageConstant.ACTION_FAIL);
        }
    }

    @RequestMapping("/findPage")
    public PageResult findPage(@RequestBody QueryPageBean queryPageBean){
        try {
            PageResult pageResult = permissionService.pageQuery(queryPageBean);
            return pageResult;
        }catch (Exception e){
            e.printStackTrace();
            return new PageResult(0L,null);
        }

    }

    @PreAuthorize("hasAnyAuthority('PERMISSION_DELETE')") // 权限校验
    @RequestMapping("/delete")
    public Result delete(Integer id){
        try{
            // 调用Service删除检查项
            permissionService.deleteById(id);
            // 成功，返回预定义成功消息
            return new Result(true,MessageConstant.ACTION_SUCCESS);
        }catch(Exception e){
            e.printStackTrace();
            // 失败，返回异常消息
            return new Result(false,e.getMessage());
        }
    }

    @RequestMapping("/findById")
    public Result findById(Integer id){
        try{
            // 调用Service,基于ID获取检查项对象
            Permission permission = permissionService.findById(id);
            // 成功，返回检查项对象数据
            return new Result(true,MessageConstant.ACTION_SUCCESS,permission);
        }catch(Exception e){
            e.printStackTrace();
            // 失败，返回操作失败消息
            return new Result(false, MessageConstant.ACTION_FAIL);
        }
    }

    @PreAuthorize("hasAnyAuthority('PERMISSION_EDIT')") // 权限校验
    @RequestMapping("/edit")
    public Result edit(@RequestBody Permission permission){
        try{
            // 调用Service，更新数据
            permissionService.edit(permission);
            // 成功，返回编辑成功消息
            return new Result(true,MessageConstant.EDIT_CHECKITEM_SUCCESS);
        }catch(Exception e){
            e.printStackTrace();
            // 失败，返回编辑失败消息
            return new Result(false,MessageConstant.EDIT_CHECKITEM_FAIL);
        }
    }

    @RequestMapping("/findAll")
    public Result findAll(){
        try {
            List<Permission> permissions = permissionService.findAll();
            return new Result(true,MessageConstant.ACTION_SUCCESS,permissions);
        }catch (Exception e){
            e.printStackTrace();
            return new Result(false,MessageConstant.ACTION_FAIL);
        }

    }
}
