package org.lanqiao.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import org.lanqiao.constant.MessageConstant;
import org.lanqiao.entity.PageResult;
import org.lanqiao.entity.QueryPageBean;
import org.lanqiao.entity.Result;
import org.lanqiao.pojo.Menu;
import org.lanqiao.service.MenuService;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/menu")
public class MenuController {
    @Reference
    private MenuService menuService;

    @RequestMapping("/add")
    public Result add(@RequestBody Menu menu) {
        try {
            menuService.add(menu);
            return new Result(true, MessageConstant.ADD_MENU_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.ADD_MENU_FAIL);
        }
    }

    @RequestMapping("/findPage")
    public PageResult findPage(@RequestBody QueryPageBean queryPageBean) {
        try {
            PageResult pageResult = menuService.pageQuery(queryPageBean.getCurrentPage(),
                    queryPageBean.getPageSize(), queryPageBean.getQueryString());
            return pageResult;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new PageResult(0l, null);
    }

    @RequestMapping("/findById")
    private Result findById(Integer id) {
        try {
            Menu menu = menuService.findById(id);
            return new Result(true, MessageConstant.ACTION_SUCCESS, menu);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.ACTION_FAIL);
        }
    }

    @RequestMapping("/edit")
    public Result edit(@RequestBody Menu menu) {
        try {
            menuService.edit(menu);
            return new Result(true, MessageConstant.EDIT_MENU_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.EDIT_MENU_FAIL);
        }
    }

    @RequestMapping("/delete")
    public Result deleteById(Integer id) {
        try {
            menuService.deleteById(id);
            System.out.println("接收到的==" + id);
            return new Result(true, MessageConstant.DELETE_MENU_SUCCESS);
        } catch (Exception e) {
            return new Result(false, MessageConstant.DELETE_MENU_FAIL);
        }
    }

    @RequestMapping("/findAll")
    @ResponseBody
    public Result findAll() {
        try {
            List<Menu> list = menuService.findAll();
            return new Result(true, MessageConstant.QUERY_MENU_SUCCESS, list);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.QUERY_MENU_FAIL);
        }
    }
}
