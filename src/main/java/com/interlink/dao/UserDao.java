package com.interlink.dao;

import com.interlink.entity.User;
import org.springframework.data.neo4j.conversion.Result;
import org.springframework.data.neo4j.repository.GraphRepository;

/**
 * Created by DmitriyS on 23/06/2015.
 */
public interface UserDao extends GraphRepository<User> {
    void createUser(User user);
    Result findAllUsersWhichCurrentKnows(Long id);
}
