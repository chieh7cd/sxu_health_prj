package org.lanqiao.service;

import org.lanqiao.entity.Result;

import java.util.Map;

/**
 * 移动体检预约业务
 */
public interface OrderService {
    public Result order(Map map) throws Exception;
    public Map findById(Integer id) throws Exception;
}
