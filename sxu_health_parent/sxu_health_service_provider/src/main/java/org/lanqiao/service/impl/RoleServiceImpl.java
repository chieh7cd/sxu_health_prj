package org.lanqiao.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.lanqiao.dao.RoleDao;
import org.lanqiao.entity.PageResult;
import org.lanqiao.entity.QueryPageBean;
import org.lanqiao.pojo.Role;
import org.lanqiao.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service(interfaceClass = RoleService.class)
@Transactional
public class RoleServiceImpl implements RoleService {

    @Autowired
    private RoleDao roleDao;

    public void add(@RequestBody Role role, Integer[] permissionIds, Integer[] menuIds) {
        roleDao.add(role);
        System.out.println(role);
        Map<String,Integer> map = new HashMap<>();
        for (Integer permissionId : permissionIds) {
            map.clear();
            map.put("role_id",role.getId());
            map.put("permission_id",permissionId);
            roleDao.addRoleAndPermission(map);
        }
        for (Integer  menuId: menuIds) {
            map.clear();
            map.put("role_id",role.getId());
            map.put("menu_id",menuId);
            roleDao.addRoleAndMenu(map);
        }
    }


    public PageResult pageQuery(QueryPageBean queryPageBean) {
        PageHelper.startPage(queryPageBean.getCurrentPage(),queryPageBean.getPageSize());
        Page<Role> page = roleDao.selectByCondition(queryPageBean.getQueryString());
        return new PageResult(page.getTotal(),page.getResult());
    }


    public List<Integer> findPermissionIdsByRoleId(Integer id) {
        return roleDao.findPermissionIdsByRoleId(id);
    }


    public List<Integer> findMenuIdsByRoleId(Integer id) {
        return roleDao.findMenuIdsByRoleId(id);
    }


    public Role findById(Integer id) {
        return roleDao.findById(id);
    }


    public void edit(Role role, Integer[] permissionIds, Integer[] menuIds) {
        // 保存检查组信息
        roleDao.edit(role);
        // 删除检查组之前关联关系
        roleDao.deletePermissionListById(role.getId());
        roleDao.deleteMenuListById(role.getId());
        // 保存新的关系
        Map map = new HashMap();
        for (Integer permissionId : permissionIds) {
            map.clear();
            map.put("role_id",role.getId());
            map.put("permission_id",permissionId);
            roleDao.addRoleAndPermission(map);
        }
        for (Integer  menuId: menuIds) {
            map.clear();
            map.put("role_id",role.getId());
            map.put("menu_id",menuId);
            roleDao.addRoleAndMenu(map);
        }
    }


    public void deleteById(Integer id) {
        roleDao.deletePermissionListById(id);
        roleDao.deleteMenuListById(id);
        roleDao.deleteRoleById(id);
    }


    public List<Role> findAll() {
        return roleDao.findAll();
    }


}
