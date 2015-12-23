package com.thecodeinside.easyfactory.core;

import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.thecodeinside.easyfactory.FactoryReference;
import com.thecodeinside.easyfactory.core.model.Address;
import com.thecodeinside.easyfactory.core.model.Node;
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

    @Test
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
        nodeFactory.addAttribute(new Attribute<FactoryReference>("nextNode", new FactoryReference(nextNode)));

        return nodeFactory;
    }
}
