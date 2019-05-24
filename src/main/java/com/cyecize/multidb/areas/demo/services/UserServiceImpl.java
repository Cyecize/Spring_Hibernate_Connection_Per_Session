package com.cyecize.multidb.areas.demo.services;

import com.cyecize.multidb.areas.demo.bindingModels.CreateUserBindingModel;
import com.cyecize.multidb.areas.demo.constants.AppConstants;
import com.cyecize.multidb.areas.demo.entities.User;
import com.cyecize.multidb.areas.demo.enums.RoleType;
import com.cyecize.multidb.areas.demo.repositories.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    private final RoleService roleService;

    private final UserRepository repository;

    private final ModelMapper modelMapper;

    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    public UserServiceImpl(RoleService roleService, UserRepository repository, ModelMapper modelMapper, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.roleService = roleService;
        this.repository = repository;
        this.modelMapper = modelMapper;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @Override
    public User createUser(CreateUserBindingModel bindingModel) {
        User user = this.modelMapper.map(bindingModel, User.class);
        user.addRole(this.roleService.find(RoleType.ROLE_USER));

        this.repository.persist(user);

        return user;
    }

    @Override
    public User findById(Long id) {
        return this.repository.find(id);
    }

    @Override
    public User findByEmailOrUsername(String handle) {
        return this.repository.findByUsernameOrEmail(handle);
    }

    @Override
    public List<User> findAll() {
        return this.repository.findAll();
    }

    @Override
    public UserDetails loadUserByUsername(String handle) throws UsernameNotFoundException {
        User user = this.findByEmailOrUsername(handle);
        if (user == null) {
            throw new UsernameNotFoundException("Username does not exist");
        }

        return user;
    }

    @Override
    public void initDbValues() {
        if (this.repository.count() > 0) {
            return;
        }

        User user = new User();
        user.setUsername(AppConstants.DEFAULT_ADMIN_DEFAULT_USERNAME);
        user.setPassword(this.bCryptPasswordEncoder.encode(AppConstants.DEFAULT_ADMIN_DEFAULT_PASSWORD));
        user.setEmail(AppConstants.DEFAULT_ADMIN_DEFAULT_EMAIL);

        user.addRole(this.roleService.find(RoleType.ROLE_USER));
        user.addRole(this.roleService.find(RoleType.ROLE_ADMIN));

        this.repository.persist(user);
    }
}
