package com.interlink;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.stream.LongStream;

@Service(value = "userService")
public class UserServiceImpl implements UserService {

    @Autowired
    private PersonRepository personRepository;

    @Override
    @Transactional
    public void populateDatabase() {
        final long count = 1_000_000;
        LongStream.rangeClosed(1L, count).forEach(userId -> {
            Person person = new Person("user #" + userId);
            personRepository.save(person);
        });
    }
}
