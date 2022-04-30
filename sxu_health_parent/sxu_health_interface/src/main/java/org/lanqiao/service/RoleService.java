package org.lanqiao.service;



import org.lanqiao.entity.PageResult;
import org.lanqiao.entity.QueryPageBean;
import org.lanqiao.pojo.Role;

import java.util.List;

public interface RoleService {

    public void add(Role role, Integer[] permissionIds, Integer[] menuIds);

    public PageResult pageQuery(QueryPageBean queryPageBean);

    public List<Integer> findPermissionIdsByRoleId(Integer id);

    public List<Integer> findMenuIdsByRoleId(Integer id);

    public Role findById(Integer id);

    public void edit(Role role, Integer[] permissionIds, Integer[] menuIds);

    public void deleteById(Integer id);

    public List<Role> findAll();
}
