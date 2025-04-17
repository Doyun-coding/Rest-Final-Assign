package com.nhnacademy.restfinalassign.model.domain;

import com.nhnacademy.restfinalassign.model.type.Role;

public class Member {

    private String id;
    private String name;
    private String password;
    private Integer age;
    private Role role;

    public Member() {}

    public Member(String id, String name, String password, Integer age, Role role) {
        this.id = id;
        this.name = name;
        this.password = password;
        this.age = age;
        this.role = role;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }
}
