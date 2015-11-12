package com.wrpinheiro.easyfactory.core.model;

import java.util.Map;

/**
 * @author Wellington Pinheiro <wellington.pinheiro@walmart.com>
 */
public class AttributesList {
    private Map<String, Attribute<?>> attributes;

    public Attribute<?> get(String id) {
        return attributes.get(id);
    }
}
