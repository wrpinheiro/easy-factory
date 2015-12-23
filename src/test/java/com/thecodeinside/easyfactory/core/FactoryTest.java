package com.thecodeinside.easyfactory.core;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import com.thecodeinside.easyfactory.FactoryReference;
import com.thecodeinside.easyfactory.core.model.Address;
import com.thecodeinside.easyfactory.core.model.User;

public class FactoryTest {
    
    private Factory<User> buildSimpleUserFactory() {
        Factory<User> userFactory = new Factory<>(new FactoryManager(FactoryTest.class.getName()), "simple_user");

        userFactory.setFullQualifiedClassName(User.class.getName());
        userFactory.addAttribute(new Attribute<Integer>("id", 12121212));
        userFactory.addAttribute(new Attribute<String>("nickname", "john.doe"));
        userFactory.addAttribute(new Attribute<String>("email", "john.doe@doe.com"));
        userFactory.addAttribute(new Attribute<String>("name", "John Doe"));
        
        return userFactory;
    }
    
    @Test
    public void build_must_create_an_user_instance() {
        Factory<User> userFactory = buildSimpleUserFactory();

        User user = userFactory.build();

        assertNotNull(user);
        assertEquals(12121212, user.getId().intValue());
        assertEquals("john.doe", user.getNickname());
        assertEquals("john.doe@doe.com", user.getEmail());
        assertEquals("John Doe", user.getName());
    }
    
    @Test
    public void build_must_create_the_user_instance_from_factory_loaded_through_factory_manager() {
        Factory<User> userFactory = buildSimpleUserFactory();

        User user = userFactory.build();

        assertNotNull(user);
        assertEquals(12121212, user.getId().intValue());
        assertEquals("john.doe", user.getNickname());
        assertEquals("john.doe@doe.com", user.getEmail());
        assertEquals("John Doe", user.getName());
    }
    
    public void build_must_create_a_factory_with_simple_relationship() {
        FactoryManager fm = new FactoryManager(this.getClass().getName());

        Factory<Address> addressFactory = new Factory<Address>(fm, "address");
        addressFactory.setFullQualifiedClassName(Address.class.getName());
        addressFactory.addAttribute(new Attribute<String>("street", "Carl Peter St."));

        fm.addFactory(addressFactory);

        Factory<User> userFactory = new Factory<User>(fm, "user_with_address");
        userFactory.setFullQualifiedClassName(User.class.getName());
        userFactory.addAttribute(new Attribute<FactoryReference>("address", new FactoryReference("address")));

        fm.addFactory(userFactory);

        User user = userFactory.build();

        assertNotNull(user);
        assertNotNull(user.getAddress());
        assertEquals("Carl Peter St.", user.getAddress().getStreet());        
    }
}
