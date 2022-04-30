package org.lanqiao.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;

import org.lanqiao.dao.MenuDao;
import org.lanqiao.entity.PageResult;
import org.lanqiao.pojo.Menu;
import org.lanqiao.service.MenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@Service(interfaceClass = MenuService.class)
@Transactional
public class MenuServiceImpl implements MenuService {

    @Autowired
    private MenuDao menuDao;

    @Override
    public void add(@RequestBody Menu menu) {
        menuDao.add(menu);
    }

    @Override
    public PageResult pageQuery(Integer currentPage, Integer pageSize, String queryString) {
        // 使用分页插件PageHelper
        PageHelper.startPage(currentPage, pageSize);
        // 获取分页数据
        Page<Menu> page = menuDao.selectByCondition(queryString);
        // 封装分页返回对象
        return new PageResult(page.getTotal(), page.getResult());
    }

    @Override
    public List<Menu> findAll() {
        return menuDao.findAll();
    }

    @Override
    public Menu findById(Integer id) {
        return menuDao.findById(id);
    }


    @Override
    public void deleteById(Integer id) {
        long count = menuDao.countMenuById(id);
        Integer a = menuDao.findparent(id);
        Long pareId = menuDao.findPareId(id);
        if (count > 0 || a != null || pareId > 0) {
            throw new RuntimeException("当前检查项有数据，不能删除");
        } else {

            menuDao.deleteById(id);
        }
    }


    @Override
    public void edit(Menu menu) {
        menuDao.edit(menu);
    }
}
