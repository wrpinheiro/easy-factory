package com.thecodeinside.easyfactory.loader;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.stream.Stream;

import org.junit.Test;

import com.thecodeinside.easyfactory.FactoryReference;
import com.thecodeinside.easyfactory.core.Attribute;
import com.thecodeinside.easyfactory.core.Factory;
import com.thecodeinside.easyfactory.core.FactoryManager;
import com.thecodeinside.easyfactory.core.model.User;

public class FileSystemLoaderTest {
    private static FactoryManager factoryManager;

    {
        FileSystemLoader fsLoader = new FileSystemLoader();
        fsLoader.loadFactories();

        factoryManager = fsLoader.factoryManager();
    }

    @Test
    public void loadFactories_must_load_a_factory_with_its_references() {
        Factory<User> userFactory = factoryManager.getFactory("user_with_address_relation");

        assertNotNull(userFactory);

        Attribute<?> attribute = userFactory.getAttributes().get("address");

        assertNotNull(attribute);
        assertTrue(attribute.getValue() instanceof FactoryReference);

        FactoryReference factoryReference = (FactoryReference) attribute.getValue();
        assertNotNull(factoryReference);
        assertEquals("address", factoryReference.getReferences()[0]);
    }

    @Test
    public void loadFactories_must_get_a_factory_for_simple_user() {
        Factory<User> userFactory = factoryManager.getFactory("simple_user");

        assertNotNull(userFactory);
        assertEquals("simple_user", userFactory.getName());
    }

    @Test
    public void loadFactories_must_build_an_instance_from_simple_user() {
        User user = factoryManager.build("simple_user");

        assertNotNull(user);
        assertEquals(10203040, user.getId().intValue());
        assertEquals("john.doe", user.getNickname());
        assertEquals("john.doe@doe.com", user.getEmail());
        assertEquals("John Doe", user.getName());
    }

    @Test
    public void loadFactories_must_build_a_factory_with_reference() {
        FileSystemLoader newFsLoader = new FileSystemLoader();
        newFsLoader.loadFactories();

        User user = newFsLoader.factoryManager().build("user_with_address_relation");

        assertNotNull(user);
        assertEquals(31318080, user.getId().intValue());
        assertNotNull(user.getAddress());
        assertEquals("Mountain St", user.getAddress().getStreet());
    }
    
    @Test
    public void loadFactories_must_build_a_factory_with_an_array_of_strings() {
        FileSystemLoader newFsLoader = new FileSystemLoader();
        newFsLoader.loadFactories();

        User user = newFsLoader.factoryManager().build("user_with_permissions");

        assertNotNull(user);
        assertEquals(3452904, user.getId().intValue());
        assertEquals("john.doe", user.getNickname());
        Stream.of(user.getPermissions()).forEach(System.out::println);
        assertArrayEquals(new String[] {"operations", "remote", "joker"}, user.getPermissions());
    }
    
    @Test
    public void loadFactories_must_build_a_factory_with_an_array_of_integers_and_force_coercion() {
        FileSystemLoader newFsLoader = new FileSystemLoader();
        newFsLoader.loadFactories();

        User user = newFsLoader.factoryManager().build("user_with_integer_permissions");

        assertNotNull(user);
        assertEquals(3452904, user.getId().intValue());
        assertEquals("john.doe", user.getNickname());
        assertArrayEquals(new String[] {"10", "20", "-5"}, user.getPermissions());
    }
}
