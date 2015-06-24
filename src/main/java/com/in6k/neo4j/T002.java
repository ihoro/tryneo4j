package com.in6k.neo4j;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * Simple selection of few nodes.
 *
 * Created by igoro on 23/06/2015.
 */
public class T002 {
    public static void main(String[] args) {
        ApplicationContext context = new AnnotationConfigApplicationContext(Config.class);
        System.out.println("context loaded");
        PersonRepository personRepository = context.getBean(PersonRepository.class);
        Iterable<Person> persons = personRepository.findByName("user #1");
        persons.forEach(p -> System.out.println("Person: " + p.toString()));
    }
}
