package org.lanqiao.dao;

import com.github.pagehelper.Page;
import org.apache.ibatis.annotations.Param;
import org.lanqiao.pojo.Permission;

import java.util.List;
import java.util.Set;

public interface PermissionDao {
    public Set<Permission> findByRoleId(Integer roleId);

    public void add(Permission permission);

    public Page<Permission> selectByCondition(@Param("queryString") String queryString);

    public Long countPermissionsById(@Param("id") Integer id);

    public void deletePermissionById(@Param("id") Integer id);

    public Permission findById(@Param("id") Integer id);

    public void edit(Permission permission);

    public List<Permission> findAll();

}
