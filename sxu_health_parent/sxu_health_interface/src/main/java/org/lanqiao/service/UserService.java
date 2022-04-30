package org.lanqiao.service;

import org.lanqiao.entity.PageResult;
import org.lanqiao.entity.QueryPageBean;
import org.lanqiao.pojo.User;

import java.util.List;
import java.util.Map;

public interface UserService {

    public User findByUsername(String username);

    boolean login(String username, String password);

    void add(User user, Integer[] roleIds);

    PageResult pageQuery(QueryPageBean queryPageBean);

    User findById(Integer id);

    List<Integer> findRoleIdsByUserId(Integer id);

    void edit(User user, Integer[] roleIds);

    void deleteById(Integer id);

    List<Map<String, Object>> getMenuListByUsername(String username);

}
