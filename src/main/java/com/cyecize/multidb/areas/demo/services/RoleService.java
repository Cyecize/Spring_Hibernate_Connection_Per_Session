package com.cyecize.multidb.areas.demo.services;

import com.cyecize.multidb.areas.conn.contracts.DbInitable;
import com.cyecize.multidb.areas.demo.entities.Role;
import com.cyecize.multidb.areas.demo.enums.RoleType;

import java.util.List;

public interface RoleService extends DbInitable {

    Role find(Long id);

    Role find(RoleType roleType);

    Role find(String roleName);

    List<Role> findAll();
}
