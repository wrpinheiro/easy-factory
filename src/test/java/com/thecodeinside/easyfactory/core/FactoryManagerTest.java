package com.thecodeinside.easyfactory.core;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import com.thecodeinside.easyfactory.core.model.User;

public class FactoryManagerTest {

    @Test
    public void addFactory_must_add_a_factory_to_factory_manager() {
        FactoryManager fm = new FactoryManager(this.getClass().getName());

        Factory<User> userFactory = new Factory<User>(fm, "user");
        userFactory.setFullQualifiedClassName(User.class.getName());
        userFactory.addAttribute(new Attribute<Integer>("id", 15423));

        fm.addFactory(userFactory);

        Factory<User> otherUserFactory = fm.getFactory("user");

        assertNotNull(otherUserFactory);
        assertEquals("user", otherUserFactory.getName());
        assertEquals(User.class.getName(), otherUserFactory.getFullQualifiedClassName());
        assertEquals(15423, otherUserFactory.getAttributes().get("id").getValue());
    }
}
