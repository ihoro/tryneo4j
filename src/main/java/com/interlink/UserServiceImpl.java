package com.interlink;

import com.interlink.dao.UserDao;
import com.interlink.entity.User;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.neo4j.conversion.Result;
import org.springframework.data.neo4j.core.GraphDatabase;
import org.springframework.data.neo4j.rest.SpringCypherRestGraphDatabase;
import org.springframework.data.neo4j.support.Neo4jTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

@Service
@Qualifier(value = "userService")
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDao userDao;

    @Autowired
    private SpringCypherRestGraphDatabase springCypherRestGraphDatabase;

    @Override
    @Transactional
    public void populateDatabase() {
        Result<User> result = userDao.findAll();
        for (User user : result) {
            System.out.println(String.format("id: %d, name: %s", user.getId(), user.getName()));
        }
        User user = userDao.findOne(1L);

        User josh = userDao.findOne(6L);
        if (josh != null) {
            user.getFriends().add(josh);
            userDao.save(user);
        }

        if (user != null) {
            System.out.println(user.getId());
            if (user.getFriends() != null) {
                for (User friend : user.getFriends()) {
                    System.out.println(String.format("%s is friend of %s", StringUtils.isBlank(friend.getName()) ? friend.getId() : friend.getName(), user.getName()));
                }
            }
            Result result1 = userDao.findAllUsersWhichCurrentKnows(user.getId());
            if (result1 != null) {
                for (User person : result1) {

                }
            }
            return;
        }
        user = new User(1L, "Jane", "Aniston", "jane@test.com", "test2");
        userDao.createUser(user);

        System.out.println(user.getId());
    }

    @Override
    @Transactional
    public void createUser() {
        Neo4jTemplate neo4jTemplate = new Neo4jTemplate((GraphDatabase) springCypherRestGraphDatabase);
        org.springframework.data.neo4j.conversion.Result<Map<String, Object>> query = neo4jTemplate.query("start n=node(*) return n", null);

        User user = new User();
        user.setFirstName("test");
        user.setLastName("user");
        userDao.createUser(user);
    }
}
