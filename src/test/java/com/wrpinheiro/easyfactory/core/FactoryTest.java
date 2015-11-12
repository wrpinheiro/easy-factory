package com.wrpinheiro.easyfactory.core;

import com.wrpinheiro.easyfactory.core.model.AttributesList;
import com.wrpinheiro.easyfactory.core.model.Factory;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class FactoryTest {

    private void assertIds(AttributesList attributes, String ... ids) {
        for (int i = 0; i < ids.length; i++) {
            assertEquals(ids[i], attributes.get(ids[i]).getId());
        }
    }

    @Test
    public void testLoadUser() {
        Factory factory = FactoryManager.load("simple_user");

        AttributesList attributes = factory.getAttributes();

        assertNotNull(attributes);
        assertEquals(5, attributes);

        assertIds(attributes, "id", "nickname", "email", "name", "address");
        assertEquals(1234, attributes.get("id").getId());
        assertEquals("joseph", attributes.get("nickname").getId());
        assertEquals("joseph@josephs.com", attributes.get("email").getId());
        assertEquals("Joseph Climber", attributes.get("name").getId());
        assertEquals("an address", attributes.get("address").getId());
    }
}
