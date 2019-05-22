package com.cyecize.multidb.areas.demo.repositories;

import com.cyecize.multidb.areas.conn.services.SessionDbService;
import com.cyecize.multidb.areas.demo.entities.User;
import com.cyecize.multidb.repository.BaseRepository;
import com.cyecize.multidb.repository.utils.NotNullPredicateList;
import org.springframework.stereotype.Service;

@Service
public class UserRepository extends BaseRepository<User, Long> {
    private static final String USERNAME_FIELD_NAME = "username";

    private static final String EMAIL_FIELD_NAME = "email";

    public UserRepository(SessionDbService sessionDbConnectionService) {
        super(sessionDbConnectionService);
    }

    public User findByUsernameOrEmail(String handle) {
        return super.queryBuilderSingle((query, userRoot) ->
                query.where(new NotNullPredicateList(
                        super.criteriaBuilder.equal(userRoot.get(USERNAME_FIELD_NAME), handle),
                        super.criteriaBuilder.or(super.criteriaBuilder.equal(userRoot.get(EMAIL_FIELD_NAME), handle))
                ).toArray())
        );
    }
}
