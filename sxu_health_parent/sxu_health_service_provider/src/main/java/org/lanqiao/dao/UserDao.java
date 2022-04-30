package org.lanqiao.dao;

import com.github.pagehelper.Page;
import org.apache.ibatis.annotations.Param;
import org.lanqiao.pojo.User;

import java.util.List;
import java.util.Map;

public interface UserDao {

    public User findByUsername(String username);

    public User findById(@Param("id") Integer id);

    public void add(User user);

    public void addUserAndRole(Map<String,Integer> map);

    public Page<User> selectByCondition(@Param("queryString") String queryString);

    public List<Integer> findRoleIdsByUserId(@Param("id") Integer id);

    public void edit(User user);

    public void deleteRolesListByIds(@Param("id") Integer id);

    public void deleteUserById(@Param("id") Integer id);

    public List<Map<String,Object>> getMenuFuByUsername(@Param("username") String username);

    public List<Map<String,Object>> getMenuZiByPath(@Param("path") String path);

}
