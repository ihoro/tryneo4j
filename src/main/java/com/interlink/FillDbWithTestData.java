package com.interlink;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * Created by DmitriyS on 23/06/2015.
 */
public class FillDbWithTestData {

    public static void main(String[] args) {
        ApplicationContext context = new AnnotationConfigApplicationContext(Config.class);
        System.out.println("context loaded");
        UserService userService = context.getBean(UserService.class);
        userService.populateDatabase();
    }
}
