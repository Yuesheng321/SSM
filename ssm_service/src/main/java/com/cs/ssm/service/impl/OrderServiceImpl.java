package com.cs.ssm.service.impl;

import com.cs.ssm.dao.IOrdersDao;
import com.cs.ssm.domain.Orders;
import com.cs.ssm.service.IOrderService;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class OrderServiceImpl implements IOrderService {
    @Autowired
    private IOrdersDao ordersDao;

    @Override
    public List<Orders> findAll(int page, int size) throws Exception {
        // 页码值，每页显示条数
        PageHelper.startPage(page, size);
        return ordersDao.findAll();
    }

    @Override
    public List<Orders> findAll() throws Exception {
        return ordersDao.findAll();
    }

}
