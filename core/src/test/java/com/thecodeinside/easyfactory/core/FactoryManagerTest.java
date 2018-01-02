package com.thecodeinside.easyfactory.core;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Test;

import com.thecodeinside.easyfactory.core.model.User;

public class FactoryManagerTest {

    private Factory<User> createUserFactory(FactoryManager fm) {
        Factory<User> userFactory = new Factory<User>(fm, "user");
        userFactory.setFullQualifiedClassName(User.class.getName());
        userFactory.addAttribute(new Attribute<Integer>("id", 15423));

        fm.addFactory(userFactory);

        return userFactory;
    }

    @Test
    public void addFactory_must_add_a_factory_to_factory_manager() {
        FactoryManager fm = new FactoryManager(this.getClass().getName());

        createUserFactory(fm);

        Factory<User> otherUserFactory = fm.getFactory("user");

        assertNotNull(otherUserFactory);
        assertEquals("user", otherUserFactory.getName());
        assertEquals(User.class.getName(), otherUserFactory.setFullQualifiedClassName());
        assertEquals(15423, otherUserFactory.getAttributes().get("id").getValue());
    }

    @Test
    public void build_must_create_multiple_duplicates_of_the_same_instance() {
        FactoryManager fm = new FactoryManager(this.getClass().getName());

        Factory<User> userFactory = createUserFactory(fm);

        List<User> users = userFactory.build(3);

        assertEquals(3, users.size());

        assertEquals(Integer.valueOf(15423), users.get(0).getId());
        assertEquals(Integer.valueOf(15423), users.get(1).getId());
        assertEquals(Integer.valueOf(15423), users.get(2).getId());

        // Guarantee that are created many instances instead of one instance with multiple references
        assertTrue(users.get(0) != users.get(1));
        assertTrue(users.get(1) != users.get(2));
    }
}
