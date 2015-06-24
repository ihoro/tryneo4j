package com.in6k.neo4j;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * Mass creation of graph nodes.
 *
 * for importing data from file:
 * 1) generate data
 *      for i in {1..1000000}; do if [ $i = 1 ]; then echo "id,name"; fi; echo "$i,user #$i"; done >> test_data.csv
 * 2) import data from browser
 *       USING PERIODIC COMMIT 500
 *       LOAD CSV WITH HEADERS FROM "file:C:/cygwin/home/dmitrys/test_data.csv" AS csvLine
 *       CREATE (p:_Person { id: toInt(csvLine.id), name: csvLine.name })
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
