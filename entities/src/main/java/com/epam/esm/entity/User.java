package com.epam.esm.entity;

public class User {

    private long id;
    private String name;
    private String surname;

    public User() {
    }

    public User(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }


    public static final class UserBuilder {

        private User newUser = new User();

        public UserBuilder() {
            this.newUser = new User();
        }

        public UserBuilder buildName(String name) {
            newUser.name = name;
            return this;
        }

        public UserBuilder buildId(long id) {
            newUser.id = id;
            return this;
        }

        public UserBuilder buildSurname(String surname) {
            newUser.surname = surname;
            return this;
        }

        public User finishBuilding() {
            return newUser;
        }
    }
}
