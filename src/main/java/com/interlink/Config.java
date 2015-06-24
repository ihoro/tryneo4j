package com.interlink;

import org.neo4j.graphdb.GraphDatabaseService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.neo4j.config.EnableNeo4jRepositories;
import org.springframework.data.neo4j.config.Neo4jConfiguration;
import org.springframework.data.neo4j.rest.SpringCypherRestGraphDatabase;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.TransactionManagementConfigurer;

/**
 * Created by DmitriyS on 23/06/2015.
 */
@Configuration
@ComponentScan
@EnableNeo4jRepositories(basePackages = "com.interlink")
@EnableTransactionManagement
public class Config extends Neo4jConfiguration implements TransactionManagementConfigurer {
    public Config() {
        setBasePackage("com.interlink");
    }

    @Bean
    public GraphDatabaseService graphDatabaseService() {
        return new SpringCypherRestGraphDatabase("http://localhost:7474/db/data/", "neo4j", "neo");
    }

    @Override
    public PlatformTransactionManager annotationDrivenTransactionManager() {
        return neo4jTransactionManager(graphDatabaseService());
    }
}
