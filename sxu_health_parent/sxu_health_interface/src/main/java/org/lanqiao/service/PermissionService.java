package org.lanqiao.service;


import org.lanqiao.entity.PageResult;
import org.lanqiao.entity.QueryPageBean;
import org.lanqiao.pojo.Permission;

import java.util.List;

public interface PermissionService {
    public void add(Permission permission);

    public PageResult pageQuery(QueryPageBean queryPageBean);

    void deleteById(Integer id);

    public Permission findById(Integer id);

    public void edit(Permission permission);

    List<Permission> findAll();


}
