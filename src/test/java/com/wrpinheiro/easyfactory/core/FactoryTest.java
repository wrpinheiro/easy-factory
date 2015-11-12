package com.wrpinheiro.easyfactory.core;

import com.wrpinheiro.easyfactory.core.model.User;
import org.junit.Assert;
import org.junit.Test;

public class FactoryTest {
  @Test
  public void testLoadUser() {
    User user = FactoryManager.load("simple_user");

    Assert.assertNotNull(user);
  }
}
