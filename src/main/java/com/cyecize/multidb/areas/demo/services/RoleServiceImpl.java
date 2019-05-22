package com.cyecize.multidb.areas.demo.services;

import com.cyecize.multidb.areas.demo.entities.Role;
import com.cyecize.multidb.areas.demo.enums.RoleType;
import com.cyecize.multidb.areas.demo.repositories.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class RoleServiceImpl implements RoleService {

    private final RoleRepository repository;

    @Autowired
    public RoleServiceImpl(RoleRepository repository) {
        this.repository = repository;
    }

    @Override
    public Role find(Long id) {
        return this.repository.find(id);
    }

    @Override
    public Role find(RoleType roleType) {
        return this.repository.findByRoleType(roleType);
    }

    @Override
    public Role find(String roleName) {
        return this.repository.findByRoleType(
                Arrays.stream(RoleType.values())
                        .filter(rt -> rt.name().equals(roleName.toUpperCase()))
                        .findFirst().orElse(null)
        );
    }

    @Override
    public List<Role> findAll() {
        return this.repository.findAll();
    }

    @Override
    public void initDbValues() {
        if (this.repository.count() >= RoleType.values().length) {
            return;
        }

        for (RoleType roleType : RoleType.values()) {
            if (this.find(roleType) == null) {
                Role role = new Role();
                role.setRoleType(roleType);

                this.repository.persist(role);
            }
        }
    }
}
