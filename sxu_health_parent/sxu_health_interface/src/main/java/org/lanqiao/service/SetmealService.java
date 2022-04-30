package org.lanqiao.service;

import org.lanqiao.entity.PageResult;
import org.lanqiao.entity.QueryPageBean;
import org.lanqiao.pojo.Setmeal;

import java.util.List;
import java.util.Map;

public interface SetmealService {
    public void add(Setmeal setmeal, Integer[] checkgroupIds);
    public PageResult pageQuery(QueryPageBean queryPageBean);
    List<String> findIdInMiddleTable(String setMealId);
    public void edit(Setmeal setmeal, Integer[] checkgroupIds, String tempImgId);
    public void delete(Integer setmealId, String imgId);
    public List<Map<String,Object>> findSetmealCount();

    // 移动端
    public List<Setmeal> findAll();
    public Setmeal findById(int id);
}
