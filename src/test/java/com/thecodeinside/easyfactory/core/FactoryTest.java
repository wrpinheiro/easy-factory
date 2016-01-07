package com.thecodeinside.easyfactory.core;

import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.thecodeinside.easyfactory.core.model.Address;
import com.thecodeinside.easyfactory.core.model.Node;
import com.thecodeinside.easyfactory.core.model.User;
import com.thecodeinside.easyfactory.core.model.UserWithAddresses;

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

    @Test
    public void build_must_create_a_user_factory_with_an_array_of_permissions() {
        Factory<User> userFactory = buildSimpleUserFactory();

        String[] values = { "operations", "remote", "joker" };

        Attribute<String[]> operations = new Attribute<>("permissions", values);

        userFactory.addAttribute(operations);

        User user = userFactory.build();

        assertNotNull(user);
        assertEquals(12121212, user.getId().intValue());
        assertNotNull(user.getPermissions());
        assertEquals(3, user.getPermissions().length);
        assertEquals("operations", user.getPermissions()[0]);
        assertEquals("remote", user.getPermissions()[1]);
        assertEquals("joker", user.getPermissions()[2]);
    }

    @Test
    public void build_must_create_a_factory_with_simple_relationship() {
        FactoryManager fm = new FactoryManager(this.getClass().getName());

        Factory<Address> addressFactory = new Factory<Address>(fm, "address");
        addressFactory.setFullQualifiedClassName(Address.class.getName());
        addressFactory.addAttribute(new Attribute<String>("street", "Carl Peter St."));

        fm.addFactory(addressFactory);

        Factory<User> userFactory = new Factory<User>(fm, "user_with_address");
        userFactory.setFullQualifiedClassName(User.class.getName());
        userFactory.addAttribute(new Attribute<FactoryReference>("address", new FactoryReference(fm, "address")));

        fm.addFactory(userFactory);

        User user = userFactory.build();

        assertNotNull(user);
        assertNotNull(user.getAddress());
        assertEquals("Carl Peter St.", user.getAddress().getStreet());
    }

    @Test
    public void build_must_create_a_factory_with_a_list_of_relationships() {
        FactoryManager fm = new FactoryManager(this.getClass().getName());

        Factory<Address> homeAddressFactory = new Factory<Address>(fm, "home_address");
        homeAddressFactory.setFullQualifiedClassName(Address.class.getName());
        homeAddressFactory.addAttribute(new Attribute<String>("street", "Carl Peter St."));

        fm.addFactory(homeAddressFactory);

        Factory<Address> workAddressFactory = new Factory<Address>(fm, "work_address");
        workAddressFactory.setFullQualifiedClassName(Address.class.getName());
        workAddressFactory.addAttribute(new Attribute<String>("street", "Mountain St."));

        fm.addFactory(workAddressFactory);

        Factory<UserWithAddresses> userFactory = new Factory<UserWithAddresses>(fm, "user_with_addresses");
        userFactory.setFullQualifiedClassName(UserWithAddresses.class.getName());
        userFactory.addAttribute(new Attribute<FactoryReference>("addresses", new FactoryReference(fm, "home_address", "work_address")));

        fm.addFactory(userFactory);

        UserWithAddresses user = userFactory.build();

        assertNotNull(user);

        assertEquals(2, user.getAddresses().size());
        assertEquals("Carl Peter St.", user.getAddresses().get(0).getStreet());
        assertEquals("Mountain St.", user.getAddresses().get(1).getStreet());
    }

    @Test
    public void build_tow_models_with_cyclic_dependency() {
        FactoryManager fm = new FactoryManager(FactoryTest.class.getName());

        Factory<Node> nodeAFactory = buildNodeFactory("nodeA", "nodeB", fm);
        Factory<Node> nodeBFactory = buildNodeFactory("nodeB", "nodeA", fm);

        fm.addFactories(asList(nodeAFactory, nodeBFactory));

        Node nodeA = nodeAFactory.build();

        assertNotNull(nodeA);
        assertEquals("nodeA", nodeA.getName());
        assertEquals("nodeB", nodeA.getNextNode().getName());
        assertTrue(nodeA == nodeA.getNextNode().getNextNode());

        assertTrue(nodeA == fm.context().instance("nodeA"));
        assertTrue(nodeA.getNextNode() == fm.context().instance("nodeB"));
    }

    @Test
    public void build_three_model_with_cyclic_dependency() {
        FactoryManager fm = new FactoryManager(FactoryTest.class.getName());

        Factory<Node> nodeAFactory = buildNodeFactory("nodeA", "nodeB", fm);
        Factory<Node> nodeBFactory = buildNodeFactory("nodeB", "nodeC", fm);
        Factory<Node> nodeCFactory = buildNodeFactory("nodeC", "nodeA", fm);

        fm.addFactories(asList(nodeAFactory, nodeBFactory, nodeCFactory));

        Node nodeA = nodeAFactory.build();

        assertNotNull(nodeA);
        assertEquals("nodeA", nodeA.getName());
        assertEquals("nodeB", nodeA.getNextNode().getName());
        assertEquals("nodeC", nodeA.getNextNode().getNextNode().getName());
        assertTrue(nodeA == nodeA.getNextNode().getNextNode().getNextNode());
    }

    private Factory<Node> buildNodeFactory(String name, String nextNode, FactoryManager fm) {
        Factory<Node> nodeFactory = new Factory<>(fm, name);

        nodeFactory.setFullQualifiedClassName(Node.class.getName());
        nodeFactory.addAttribute(new Attribute<String>("name", name));
        nodeFactory.addAttribute(new Attribute<FactoryReference>("nextNode", new FactoryReference(fm, nextNode)));

        return nodeFactory;
    }
}
