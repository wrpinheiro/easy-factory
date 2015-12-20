package com.wrpinheiro.easyfactory.core;

import java.lang.reflect.InvocationTargetException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;

/**
 * @author Wellington Pinheiro <wellington.pinheiro@gmail.com>
 */
public class Factory<T> {
    private String name;
    private String fullQualifiedClassName;

    private Map<String, Attribute<?>> attributes = new HashMap<>();

    public Factory() {
    }

    public Factory(String name) {
        this.name = name;
    }

    public Attribute<?> get(String id) {
        return attributes.get(id);
    }

    public void addAttribute(Attribute<?> attribute) {
        attributes.put(attribute.getId(), attribute);
    }

    public Map<String, Attribute<?>> getAttributes() {
        return Collections.unmodifiableMap(attributes);
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public void setFullQualifiedClassName(String fqClassName) {
        this.fullQualifiedClassName = fqClassName;
    }
    
    public String setFullQualifiedClassName() {
        return fullQualifiedClassName;
    }

    @SuppressWarnings("unchecked")
    public T build() {
        try {
            Class<?> clazz = Class.forName(fullQualifiedClassName);
            T instance = (T) clazz.newInstance();

            attributes.values().forEach(attribute -> {
                try {
                    BeanUtils.setProperty(instance, attribute.getId(), attribute.getValue());
                } catch (IllegalAccessException | InvocationTargetException ex) {
                    ex.printStackTrace();
                }
            });

            return instance;
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException ex) {
            ex.printStackTrace();
        }

        return null;
    }

    @Override
    public String toString() {
        return "Factory [name=" + name + ", fullQualifiedClassName=" + fullQualifiedClassName + ", attributes="
                + attributes + "]";
    }
}
