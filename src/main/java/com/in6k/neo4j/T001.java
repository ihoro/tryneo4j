package com.in6k.neo4j;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * Mass creation of graph nodes.
 *
 * Created by DmitriyS on 23/06/2015.
 */
public class T001 {

    public static void main(String[] args) {
        ApplicationContext context = new AnnotationConfigApplicationContext(Config.class);
        System.out.println("context loaded");
        UserService userService = context.getBean(UserService.class);
        userService.populateDatabase();
    }
}
