package com.cyecize.multidb.areas.demo.entities;

import com.cyecize.multidb.areas.demo.enums.RoleType;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.*;

@Entity
@Table(name = "roles")
public class Role implements GrantedAuthority {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false, updatable = false)
    private Long id;

    @Column(name = "role_type", unique = true, nullable = false)
    @Enumerated(EnumType.STRING)
    private RoleType roleType;

    public Role() {

    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public RoleType getRoleType() {
        return this.roleType;
    }

    public void setRoleType(RoleType roleType) {
        this.roleType = roleType;
    }

    @Override
    @Transient
    public String getAuthority() {
        return this.roleType.name();
    }
}
