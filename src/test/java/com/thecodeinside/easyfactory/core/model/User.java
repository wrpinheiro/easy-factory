package com.thecodeinside.easyfactory.core.model;

import java.util.Arrays;

public class User {
    private Integer id;
    private String nickname;
    private String email;
    private String name;
    private boolean admin;
    private Address address;
    private String[] permissions;

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return this.id;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getNickname() {
        return this.nickname;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEmail() {
        return this.email;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public void setAdmin(boolean admin) {
        this.admin = admin;
    }

    public boolean getAdmin() {
        return this.admin;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }
    
    public void setPermissions(String[] permissions) {
        this.permissions = permissions;
    }

    public String[] getPermissions() {
        return permissions;
    }

    @Override
    public String toString() {
        return "User [id=" + id + ", nickname=" + nickname + ", email=" + email + ", name=" + name + ", admin=" + admin
                + ", address=" + address + ", permissions=" + Arrays.toString(permissions) + "]";
    }
}
