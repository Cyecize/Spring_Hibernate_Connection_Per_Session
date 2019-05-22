package com.cyecize.multidb.areas.demo.repositories;

import com.cyecize.multidb.areas.conn.services.SessionDbService;
import com.cyecize.multidb.areas.demo.entities.Role;
import com.cyecize.multidb.areas.demo.enums.RoleType;
import com.cyecize.multidb.repository.BaseRepository;
import com.cyecize.multidb.repository.utils.NotNullPredicateList;
import org.springframework.stereotype.Service;

@Service
public class RoleRepository extends BaseRepository<Role, Long> {

    private static final String ROLE_TYPE_FIELD_NAME = "roleType";

    public RoleRepository(SessionDbService sessionDbConnectionService) {
        super(sessionDbConnectionService);
    }

    public Role findByRoleType(RoleType roleType) {
        return super.queryBuilderSingle((query, roleRoot) ->
                query.where(super.criteriaBuilder.equal(roleRoot.get(ROLE_TYPE_FIELD_NAME), roleType)));
    }
}
