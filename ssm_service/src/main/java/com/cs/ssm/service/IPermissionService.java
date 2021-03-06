package com.cs.ssm.service;

import com.cs.ssm.domain.Permission;

import java.util.List;

public interface IPermissionService {
    List<Permission> findAll() throws Exception;

    void save(Permission permission) throws Exception;
}
