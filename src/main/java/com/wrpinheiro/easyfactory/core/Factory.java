package com.wrpinheiro.easyfactory.core;

import java.lang.reflect.InvocationTargetException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;

import com.wrpinheiro.easyfactory.FactoryReference;

/**
 * @author Wellington Pinheiro <wellington.pinheiro@gmail.com>
 */
public class Factory<T> {
    private String name;
    private String fullQualifiedClassName;

    private Map<String, Attribute<?>> attributes = new HashMap<>();
    private FactoryManager factoryManager;

    public Factory(FactoryManager factoryManager, String name) {
        this.factoryManager = factoryManager;
        
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

            attributes.values().stream().filter(Attribute::isNotReference).forEach(attribute -> {
                setBeanProperty(instance, attribute.getId(), attribute.getValue());
            });

            factoryManager.context().addInstance(this.getName(), instance);

            attributes.values().stream().filter(Attribute::isReference).forEach(attribute -> {
                Object reference = loadReference((FactoryReference)attribute.getValue());

                if (reference != null) {
                    setBeanProperty(instance, attribute.getId(), reference);
                }
            });

            return instance;
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException ex) {
            ex.printStackTrace();
        }

        return null;
    }

    public Object loadReference(FactoryReference factoryReference) {
        String referenceName = factoryReference.getReference();
        Object referenceInstance = factoryManager.context().instance(referenceName);
        if (referenceInstance == null) {
            referenceInstance = factoryManager.build(referenceName);
        }
        
        return referenceInstance;
    }

    private void setBeanProperty(T instance, String property, Object value) {
        try {
            BeanUtils.setProperty(instance, property, value);
        } catch (IllegalAccessException | InvocationTargetException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public String toString() {
        return "Factory [name=" + name + ", fullQualifiedClassName=" + fullQualifiedClassName + ", attributes="
                + attributes + "]";
    }
}
