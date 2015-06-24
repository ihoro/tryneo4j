package com.in6k.neo4j;

/**
 * Created by igoro on 23/06/2015.
 */
import org.springframework.data.repository.CrudRepository;

public interface PersonRepository extends CrudRepository<Person, String> {

    Iterable<Person> findByName(String name);

    Iterable<Person> findByTeammatesName(String name);

}
