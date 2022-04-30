package org.lanqiao.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import freemarker.template.Template;
import org.lanqiao.constant.RedisConstant;
import org.lanqiao.dao.SetmealDao;
import org.lanqiao.entity.PageResult;
import org.lanqiao.entity.QueryPageBean;
import org.lanqiao.pojo.Setmeal;
import org.lanqiao.service.SetmealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;
import redis.clients.jedis.JedisPool;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import freemarker.template.Configuration;

@Service(interfaceClass = SetmealService.class)
@Transactional
public class SetmealServiceImpl implements SetmealService {

    @Autowired
    private FreeMarkerConfigurer freeMarkerConfigurer;

    @Autowired
    private SetmealDao setmealDao;

    @Autowired
    private JedisPool jedisPool;

    //从属性文件中读取要生成的html对应的目录
    private String outPutPath = "D:\\IdeaProjects\\sxu_health_prj\\sxu_health_parent\\sxu_health_mobile\\src\\main\\webapp\\pages";

    // 新增套餐信息，同时需要关联检查组
    public void add(Setmeal setmeal, Integer[] checkgroupIds) {
        setmealDao.add(setmeal);
        Integer setmealId = setmeal.getId();
        this.setSetmealAndCheckgroup(setmealId, checkgroupIds);
        // 将图片名称保存到Redis集合中
        String fileName = setmeal.getImg();
        jedisPool.getResource().sadd(RedisConstant.SETMEAL_PIC_DB_RESOURCES, fileName);

        //当添加套餐后需要重新生成静态页面（套餐列表页面、套餐详情页面）
        generateMobileStaticHtml();
    }

    //生成当前方法所需的静态页面
    public void generateMobileStaticHtml() {
        //在生成静态页面之前需要查询数据
        List<Setmeal> list = setmealDao.findAll();

        //需要生成套餐列表静态页面
        generateMobileSetmealListHtml(list);

        //需要生成套餐详情静态页面
        generateMobileSetmealDetailHtml(list);
    }

    //生成套餐列表静态页面
    public void generateMobileSetmealListHtml(List<Setmeal> setmealList) {
        Map<String, Object> map = new HashMap<>();
        //为模板提供数据，用于生成静态页面
        map.put("setmealList", setmealList);
        generteHtml("mobile_setmeal.ftl", "m_setmeal.html", map);
    }

    //生成套餐详情静态页面（可能有多个）
    public void generateMobileSetmealDetailHtml(List<Setmeal> list) {
        for (Setmeal setmeal : list) {
            Map<String, Object> map = new HashMap();
            map.put("setmeal", setmealDao.findById(setmeal.getId()));
            generteHtml("mobile_setmeal_detail.ftl", "setmeal_detail_" + setmeal.getId() + ".html", map);
        }
    }

    //通用方法，用于生成静态页面
    public void generteHtml(String templateName, String htmlPageName,  Map<String, Object> map) {
        //获取配置对象
        Configuration configuration = freeMarkerConfigurer.getConfiguration();
        Writer out = null;
        try {
            //加载模板文件
            Template template = configuration.getTemplate(templateName);
            //构造输出流
            // 生成数据
            File docFile = new File(outPutPath + "\\" + htmlPageName);
            out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(docFile)));
            //输出文件
            template.process(map, out);
            //关流释放资源
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 套餐分页
    public PageResult pageQuery(QueryPageBean queryPageBean) {
        Integer currentPage = queryPageBean.getCurrentPage();
        Integer pageSize = queryPageBean.getPageSize();
        String queryString = queryPageBean.getQueryString();
        PageHelper.startPage(currentPage,pageSize);
        Page<Setmeal> page = setmealDao.findByCondition(queryString);
        return new PageResult(page.getTotal(),page.getResult());
    }

    //根据套餐Id查询检查组id
    @Override
    public List<String> findIdInMiddleTable(String setMealId) {
        List<String> checkgroupIds = new ArrayList<>();
        if (setMealId != null && !setMealId.equals("")) {
            checkgroupIds = setmealDao.findCheckGroupIdsBySetMealId(setMealId);
        }
        return checkgroupIds;
    }

    public void edit(Setmeal setmeal, Integer[] checkgroupIds, String tempImgId) {
        //编辑套餐
        setmealDao.editSetmeal(setmeal);
        //获取SetmealId
        Integer setmealId = setmeal.getId();
        //编辑套餐和检查组关联关系
        if (checkgroupIds != null && checkgroupIds.length > 0) {
            //先清空关联关系
            setmealDao.deleteSetmealAndCheckgroup(setmealId);
            this.setSetmealAndCheckgroup(setmealId, checkgroupIds);
        }
    }

    //删除套餐
    public void delete(Integer setmealId, String imgId) {
        //删除操作
        if (setmealId!=null&&!setmealId.equals("")){
            //先删除关联关系
            setmealDao.deleteSetmealAndCheckgroup(setmealId);
            //根据id删除套餐
            setmealDao.deleteSetmealById(setmealId);
        }
    }

    //查询套餐预约占比数据
    public List<Map<String, Object>> findSetmealCount() {
        return setmealDao.findSetmealCount();
    }

    public List<Setmeal> findAll() {
        return setmealDao.findAll();
    }

    //根据套餐ID查询套餐详情（套餐基本信息、套餐对应的检查组信息、检查组对应的检查项信息）
    public Setmeal findById(int id) {
        return setmealDao.findById(id);
    }



    //设置套餐和检查组多对多关系，操作t_setmeal_checkgroup
    public void setSetmealAndCheckgroup(Integer setmealId,Integer[] checkgroupIds){
        if(checkgroupIds != null && checkgroupIds.length > 0){
            Map<String,Integer> map = null;
            for (Integer checkgroupId : checkgroupIds) {
                map =  new HashMap<>();
                map.put("setmealId",setmealId);
                map.put("checkgroupId",checkgroupId);
                setmealDao.setSetmealAndCheckGroup(map);
            }
        }
    }

}
