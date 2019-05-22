package com.cyecize.multidb.areas.demo.services;

import com.cyecize.multidb.areas.conn.contracts.DbInitable;
import com.cyecize.multidb.areas.demo.bindingModels.CreateUserBindingModel;
import com.cyecize.multidb.areas.demo.entities.User;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

public interface UserService extends UserDetailsService, DbInitable {

    User createUser(CreateUserBindingModel bindingModel);

    User findById(Long id);

    User findByEmailOrUsername(String handle);

    List<User> findAll();
}
