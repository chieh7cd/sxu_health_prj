package org.lanqiao.service;


import org.lanqiao.entity.PageResult;
import org.lanqiao.pojo.Menu;

import java.util.List;

public interface MenuService {
    public void add(Menu menu);

    public PageResult pageQuery(Integer currentPage, Integer pageSize, String queryString);

    public List<Menu> findAll();

    public Menu findById(Integer id);

    public void deleteById(Integer id);

    public void edit(Menu menu);
}
