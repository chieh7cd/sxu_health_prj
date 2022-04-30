package org.lanqiao.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import org.lanqiao.constant.MessageConstant;
import org.lanqiao.entity.PageResult;
import org.lanqiao.entity.QueryPageBean;
import org.lanqiao.entity.Result;
import org.lanqiao.pojo.Role;
import org.lanqiao.service.RoleService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/role")
public class RoleController {

    @Reference
    private RoleService roleService;

    @PreAuthorize("hasAnyAuthority('ROLE_ADD')") // 权限校验
    @RequestMapping("/add")
    public Result add(@RequestBody Role role, Integer[] permissionIds, Integer[] menuIds) {
        try {
            roleService.add(role, permissionIds, menuIds);
            return new Result(true, MessageConstant.ACTION_SUCCESS);
        } catch (Exception e) {
            return new Result(false, MessageConstant.ACTION_FAIL);
        }

    }

    @RequestMapping("/findPage")
    public PageResult findPage(@RequestBody QueryPageBean queryPageBean) {
        try {
            // 调用Service获取分页结果数据
            PageResult pageResult = roleService.pageQuery(queryPageBean);
            // 成功，直接返回有内容的结果
            return pageResult;
        } catch (Exception e) {
            e.printStackTrace();
        }
        // 失败，返回初始化结果，记录为0，数据为空
        return new PageResult(0L, null);
    }

    @RequestMapping("/findPermissionIdsByRoleId")
    public Result findPermissionIdsByRoleId(Integer id) {
        try {
            List<Integer> list = roleService.findPermissionIdsByRoleId(id);
            return new Result(true, MessageConstant.ACTION_SUCCESS, list);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.ACTION_FAIL);
        }
    }

    @RequestMapping("/findMenuIdsByRoleId")
    public Result findMenuIdsByRoleId(Integer id) {
        try {
            List<Integer> list = roleService.findMenuIdsByRoleId(id);
            return new Result(true, MessageConstant.ACTION_SUCCESS, list);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.ACTION_FAIL);
        }
    }

    @RequestMapping("/findById")
    public Result findById(Integer id) {
        try {
            Role role = roleService.findById(id);
            return new Result(true, MessageConstant.ACTION_SUCCESS, role);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.ACTION_FAIL);
        }
    }

    // 编辑角色
    @PreAuthorize("hasAnyAuthority('ROLE_EDIT')") // 权限校验
    @RequestMapping("/edit")
    public Result edit(@RequestBody Role role, Integer[] permissionIds, Integer[] menuIds) {
        try {
            roleService.edit(role, permissionIds, menuIds);
            return new Result(true, MessageConstant.EDIT_ROLE_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.EDIT_ROLE_FAIL);
        }
    }

    @PreAuthorize("hasAnyAuthority('ROLE_DELETE')") // 权限校验
    @RequestMapping("/delete")
    public Result delete(Integer id) {
        try {
            // 调用Service删除角色
            roleService.deleteById(id);
            // 成功，返回预定义成功消息
            return new Result(true, MessageConstant.DELETE_ROLE_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            // 失败，返回异常消息
            return new Result(false, MessageConstant.DELETE_ROLE_FAIL);
        }
    }

    @RequestMapping("/findAll")
    public Result findAll() {
        try {
            List<Role> roleList = roleService.findAll();
            return new Result(true, MessageConstant.ACTION_SUCCESS, roleList);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.ACTION_FAIL);
        }
    }
}
