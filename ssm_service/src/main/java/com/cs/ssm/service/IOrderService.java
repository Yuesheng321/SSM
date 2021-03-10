package com.cs.ssm.service;

import com.cs.ssm.domain.Orders;

import java.util.List;

public interface IOrderService {

    List<Orders> findAll() throws Exception;

    List<Orders> findAll(int page, int size) throws Exception;
}
