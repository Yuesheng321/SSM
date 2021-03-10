package com.cs.ssm.service;

import com.cs.ssm.domain.Product;

import java.util.List;

public interface IProductService {
    public List<Product> findAll() throws Exception;

    void save(Product product);
}
