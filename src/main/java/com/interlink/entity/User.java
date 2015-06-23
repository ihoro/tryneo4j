package com.interlink.entity;

import org.neo4j.graphdb.Direction;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.neo4j.annotation.GraphId;
import org.springframework.data.neo4j.annotation.Indexed;
import org.springframework.data.neo4j.annotation.NodeEntity;
import org.springframework.data.neo4j.annotation.RelatedTo;

import java.util.Set;

/**
 * Created by DmitriyS on 23/06/2015.
 */
@NodeEntity
@TypeAlias(value="_User")
public class User {

    @GraphId
    @Indexed(unique=true)
    private Long id;

    private String firstName;
    private String lastName;

    @Indexed(unique=false)
    private String email;
    private String password;

    @RelatedTo(type = "FRIEND_OF", direction = Direction.INCOMING)
    private Set<User> friends;

    @RelatedTo(type = "KNOWS", direction = Direction.OUTGOING)
    private Set<User> familiarUsers;

    public User() {
    }

    public User(Long id, String firstName, String lastName, String email, String password) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setName(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public String getName() {
        return  String.format("%s %s", firstName == null ? "" : firstName, lastName == null ? "" : lastName);
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Set<User> getFriends() {
        return friends;
    }

    public void setFriends(Set<User> friends) {
        this.friends = friends;
    }

    public Set<User> getFamiliarUsers() {
        return familiarUsers;
    }

    public void setFamiliarUsers(Set<User> familiarUsers) {
        this.familiarUsers = familiarUsers;
    }
}
