package org.lanqiao.dao;

import com.github.pagehelper.Page;
import org.lanqiao.pojo.CheckGroup;
import org.lanqiao.pojo.Setmeal;

import java.util.List;
import java.util.Map;

public interface SetmealDao {
    public void add(Setmeal setmeal);
    public void setSetmealAndCheckGroup(Map map);
    public Page<Setmeal> findByCondition(String queryString);
    List<String> findCheckGroupIdsBySetMealId(String setMealId);
    public void editSetmeal(Setmeal setmeal);
    public void deleteSetmealAndCheckgroup(Integer setmealId);
    public void deleteSetmealById(Integer setmealId);
    public List<Setmeal> findAll();
    public Setmeal findById(int id);
    public List<Map<String, Object>> findSetmealCount();
}
