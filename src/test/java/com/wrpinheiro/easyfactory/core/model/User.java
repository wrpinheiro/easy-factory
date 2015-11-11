package com.wrpinheiro.easyfactory.core.model;

public class User {
  private Integer id;
  private String nickname;
  private String email;
  private String name;

  public void setId(Integer id) {
    this.id = id;
  }

  public Integer getId() {
    return this.id;
  }

  public void setNickanem(String nickname) {
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
}
