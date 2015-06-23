package com.interlink;

import com.interlink.dao.UserDao;
import com.interlink.dao.UserDaoImpl;
import org.neo4j.graphdb.GraphDatabaseService;
import org.springframework.beans.factory.annotation.Autowire;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.neo4j.config.EnableNeo4jRepositories;
import org.springframework.data.neo4j.rest.SpringCypherRestGraphDatabase;
import org.springframework.data.neo4j.support.Neo4jTemplate;

/**
 * Created by DmitriyS on 23/06/2015.
 */
@Configuration
@ComponentScan
@EnableNeo4jRepositories(basePackages = "org.springframework.data.neo4j.repository")
public class Config {

    private static final String DB_PATH = "/Temp/neo4j";

    @Bean
    public UserDao userDao() {
        return new UserDaoImpl();
    }

    @Bean(autowire = Autowire.BY_NAME)
    public UserService userService() {
        return new UserServiceImpl();
    }

    @Bean
    public GraphDatabaseService graphDatabaseService() {
        return new SpringCypherRestGraphDatabase("http://localhost:7474/db/data/", "neo4j", "1");
    }

    @Bean
    public Neo4jTemplate neo4jTemplate() {
        return new Neo4jTemplate(graphDatabaseService());
    }

}
