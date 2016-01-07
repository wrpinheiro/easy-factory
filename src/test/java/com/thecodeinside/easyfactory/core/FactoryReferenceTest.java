package com.thecodeinside.easyfactory.core;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Test;

import com.thecodeinside.easyfactory.core.model.User;

public class FactoryReferenceTest {

    @Test
    public void must_create_an_instance_with_three_copies_of_the_same_reference() {
        FactoryManager fm = new FactoryManager(this.getClass().getName());
        FactoryReference fr = new FactoryReference(fm, "user", 3);

        assertArrayEquals(new String[] { "user", "user", "user" }, fr.getReferences());
    }

    @Test
    public void must_create_three_equals_references() {
        FactoryManager fm = new FactoryManager(this.getClass().getName());

        Factory<User> userFactory = new Factory<User>(fm, "user");
        userFactory.setFullQualifiedClassName(User.class.getName());
        userFactory.addAttribute(new Attribute<String>("nickname", "john.doe"));

        fm.addFactory(userFactory);

        FactoryReference fr = new FactoryReference(fm, "user", 3);

        List<Object> instances = fr.loadReferences();

        assertEquals(3, instances.size());

        assertEquals("john.doe", ((User) instances.get(0)).getNickname());
        assertEquals(instances.get(0).hashCode(), instances.get(1).hashCode());
        assertEquals(instances.get(1).hashCode(), instances.get(2).hashCode());
    }
}
