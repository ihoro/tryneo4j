package com.interlink.dao;


import com.interlink.entity.User;
import org.neo4j.kernel.Traversal;
import org.springframework.data.neo4j.conversion.Result;
import org.springframework.stereotype.Service;

/**
 * Created by DmitriyS on 23/06/2015.
 */
@Service(value = "userDao")
public class UserDaoImpl extends BaseDao<User> implements UserDao {

    @Override
    public void createUser(User user) {
        template.save(user);
    }

    @Override
    public Result findAllUsersWhichCurrentKnows(Long id) {
        return template.traverse(template.findOne(id, getClassOfT()),
            Traversal.description()
                .depthFirst()
                .relationships(() -> "KNOWS")
        //        .evaluator(Evaluators.toDepth(5))
        );
    }

    public User findByEmail(String email) {
        return findByPropertyValue("email", email);
    }
}
