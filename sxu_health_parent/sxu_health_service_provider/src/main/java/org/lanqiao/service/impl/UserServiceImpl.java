package org.lanqiao.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.lanqiao.dao.PermissionDao;
import org.lanqiao.dao.RoleDao;
import org.lanqiao.dao.UserDao;
import org.lanqiao.entity.PageResult;
import org.lanqiao.entity.QueryPageBean;
import org.lanqiao.pojo.Permission;
import org.lanqiao.pojo.Role;
import org.lanqiao.pojo.User;
import org.lanqiao.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * 用户服务
 */
@Service(interfaceClass = UserService.class)
@Transactional
public class UserServiceImpl implements UserService {
    @Autowired
    private UserDao userDao;
    @Autowired
    private RoleDao roleDao;
    @Autowired
    private PermissionDao permissionDao;
    //根据用户名查询数据库获取用户信息和关联的角色信息，同时需要查询角色关联的权限信息
    public User findByUsername(String username) {
        User user = userDao.findByUsername(username);//查询用户基本信息，不包含用户的角色
        if(user == null){
            return null;
        }
        Integer userId = user.getId();
        //根据用户ID查询对应的角色
        Set<Role> roles = roleDao.findByUserId(userId);
        for (Role role : roles) {
            Integer roleId = role.getId();
            //根据角色ID查询关联的权限
            Set<Permission> permissions = permissionDao.findByRoleId(roleId);
            role.setPermissions(permissions);//让角色关联权限
        }
        user.setRoles(roles);//让用户关联角色
        return user;
    }


    @Override
    public boolean login(String username, String password) {
        if("admin".equals(username) && "123".equals(password)){
            return true;
        }
        return false;
    }

    @Transactional
    @Override
    public void add(User user, Integer[] roleIds) {
        userDao.add(user);
        Map<String,Integer> map = new HashMap<>();
        for (Integer roleId : roleIds) {
            map.clear();
            map.put("user_id",user.getId());
            map.put("role_id",roleId);
            userDao.addUserAndRole(map);
        }
    }

    @Override
    public PageResult pageQuery(QueryPageBean queryPageBean) {
        PageHelper.startPage(queryPageBean.getCurrentPage(),queryPageBean.getPageSize());
        Page<User> page = userDao.selectByCondition(queryPageBean.getQueryString());
        return new PageResult(page.getTotal(),page.getResult());
    }

    @Override
    public User findById(Integer id) {
        return userDao.findById(id);
    }

    @Override
    public List<Integer> findRoleIdsByUserId(Integer id) {
        return userDao.findRoleIdsByUserId(id);
    }

    @Transactional
    @Override
    public void edit(User user, Integer[] roleIds) {
        userDao.edit(user);
        userDao.deleteRolesListByIds(user.getId());
        Map<String,Integer> map = new HashMap<>();
        for (Integer roleId : roleIds) {
            map.clear();
            map.put("user_id",user.getId());
            map.put("role_id",roleId);
            userDao.addUserAndRole(map);
        }
    }

    @Transactional
    @Override
    public void deleteById(Integer id) {
        List<Integer> roleIds = userDao.findRoleIdsByUserId(id);
        for (Integer roleId : roleIds) {
            roleDao.deleteMenuListById(roleId);
            roleDao.deletePermissionListById(roleId);
        }
        userDao.deleteRolesListByIds(id);
        userDao.deleteUserById(id);
    }

    @Override
    public List<Map<String,Object>> getMenuListByUsername(String username) {
        List<Map<String,Object>> menuFuList = userDao.getMenuFuByUsername(username);
        System.out.println("menuFuList"+menuFuList);
        List<Map<String,Object>> resultMap = new ArrayList<>();
        for (Map<String, Object> map : menuFuList) {
            String path = (String)map.get("path");
            List<Map<String, Object>> menuZiList = userDao.getMenuZiByPath(path+"-");
            map.put("children",menuZiList);
            resultMap.add(map);
        }
        System.out.println(resultMap);
        return resultMap;
    }

}
