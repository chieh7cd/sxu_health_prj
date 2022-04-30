package org.lanqiao.service.impl;


import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.lanqiao.dao.CheckGroupDao;
import org.lanqiao.entity.PageResult;
import org.lanqiao.entity.QueryPageBean;
import org.lanqiao.pojo.CheckGroup;
import org.lanqiao.service.CheckGroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 检查组服务
 */
@Service(interfaceClass = CheckGroupService.class)
@Transactional
public class CheckGroupServiceImpl implements CheckGroupService {
    @Autowired
    private CheckGroupDao checkGroupDao;

    // 新增检查组，同时需要让检查组关联检查项
    public void add(CheckGroup checkGroup, Integer[] checkitemIds) {
        // 新增检查组，操作t_checkgroup表
        checkGroupDao.add(checkGroup);
        // 设置检查组和检查项的关联关系,操作t_checkgroup_checkitem表
        Integer checkGroupId = checkGroup.getId();
        // 建立检查组和检查项多对多关系
        this.setCheckGroupAndCheckItem(checkGroupId, checkitemIds);
    }

    // 分页查询
    public PageResult pageQuery(QueryPageBean queryPageBean) {
        Integer currentPage = queryPageBean.getCurrentPage();
        Integer pageSize = queryPageBean.getPageSize();
        String queryString = queryPageBean.getQueryString();
        PageHelper.startPage(currentPage, pageSize);
        Page<CheckGroup> page = checkGroupDao.findByCondition(queryString);
        return new PageResult(page.getTotal(), page.getResult());
    }

    // 根据ID查询检查组
    public CheckGroup findById(Integer id) {
        return checkGroupDao.findById(id);
    }

    // 根据检查组ID查询关联的检查项ID
    public List<Integer> findCheckItemIdsByCheckGroupId(Integer id) {
        return checkGroupDao.findCheckItemIdsByCheckGroupId(id);
    }

    // 编辑检查组信息，同时需要关联检查项
    public void edit(CheckGroup checkGroup, Integer[] checkitemIds) {
        // 修改检查组基本信息，操作检查组t_checkgroup表
        checkGroupDao.edit(checkGroup);
        // 清理当前检查组关联的检查项，操作中间关系表 t_checkgroup_checkitem表
        checkGroupDao.deleteAssociation(checkGroup.getId());
        // 重新建立当前检查组和检查项的关联关系
        // 设置检查组和检查项的关联关系,操作t_checkgroup_checkitem表
        Integer checkGroupId = checkGroup.getId();
        // 建立检查组和检查项多对多关系
        this.setCheckGroupAndCheckItem(checkGroupId, checkitemIds);
    }

    // 根据检测组ID删除检查组
    public void delete(Integer id) {
        //先删除关联关系
        checkGroupDao.deleteAssociation(id);
        //再删除检查组
        checkGroupDao.delete(id);
    }

    // 查询所有检查组
    public List<CheckGroup> findAll() {
        return checkGroupDao.findAll();
    }

    // 抽取一个方法——建立检查组和检查项多对多关系
    public void setCheckGroupAndCheckItem(Integer checkGroupId, Integer[] checkitemIds){
        if(checkitemIds != null && checkitemIds.length > 0){
            Map<String,Integer> map = null;
            for (Integer checkitemId : checkitemIds) {
                map = new HashMap<>();
                map.put("checkGroupId", checkGroupId);
                map.put("checkitemId", checkitemId);
                checkGroupDao.setCheckGroupAndCheckItem(map);
            }
        }
    }

}
