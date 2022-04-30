package org.lanqiao.dao;

import com.github.pagehelper.Page;
import org.lanqiao.pojo.Menu;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface MenuDao {
    void add(Menu menu);

    public Page<Menu> selectByCondition(@Param("queryString") String queryString);

    Menu findById(Integer id);

    void edit(Menu menu);

    void deleteById(Integer id);

    Long countMenuById(Integer menuId);

    List<Menu> findAll();

    Integer findparent(Integer id);

    Long findPareId(Integer id);

}
