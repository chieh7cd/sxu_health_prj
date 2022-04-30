package org.lanqiao.dao;

import com.github.pagehelper.Page;
import org.apache.ibatis.annotations.Param;
import org.lanqiao.pojo.Role;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface RoleDao {
    public Set<Role> findByUserId(Integer userId);

    public void add(Role role);

    public void addRoleAndPermission(Map<String, Integer> map);

    public void addRoleAndMenu(Map<String, Integer> map);

    public Page<Role> selectByCondition(@Param("queryString") String queryString);

    public List<Integer> findPermissionIdsByRoleId(Integer id);

    public List<Integer> findMenuIdsByRoleId(Integer id);

    public Role findById(Integer id);

    public void edit(Role role);

    public void deletePermissionListById(Integer id);

    public void deleteMenuListById(Integer id);

    public void deleteRoleById(Integer id);

    public List<Role> findAll();

}
