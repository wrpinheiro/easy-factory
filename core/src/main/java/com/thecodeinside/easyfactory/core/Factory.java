package com.thecodeinside.easyfactory.core;

import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.beanutils.PropertyUtils;

import com.thecodeinside.easyfactory.FactoryReference;

/**
 * A factory representing an instance with its attributes.
 * 
 * @author Wellington Pinheiro <wellington.pinheiro@gmail.com>
 *
 * @param <T> type of instance created by this factory.
 */
public class Factory<T> {
    /**
     * The name of the factory. This name should be unique for each Factory Manager.
     */
    private String name;

    /**
     * The type of the instance that will be created by this factory.
     */
    private String fullQualifiedClassName;

    /**
     * Attributes that will be populated in the instance created by this factory.
     */
    private Map<String, Attribute<?>> attributes = new HashMap<>();

    /**
     * The manager of this factory.
     */
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

    /**
     * Create an instance based on the definitions of this factory.
     * 
     * @return an instance of type T
     */
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
                List<Object> references = ((FactoryReference) attribute.getValue()).loadReferences();

                if (references != null) {
                    setBeanProperty(instance, attribute.getId(), references);
                }
            });

            return instance;
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException ex) {
            ex.printStackTrace();
        }

        return null;
    }

    /**
     * Return a list containing <code>numberOfDuplicates</code> instances.
     * 
     * @param numberOfDuplicates the number of instances to be created.
     * 
     * @return a list of type T
     */
    public List<T> build(int numberOfDuplicates) {
        List<T> instances = new ArrayList<>();

        for (int i = 0; i < numberOfDuplicates; i++) {
            T instance = build();

            instances.add(instance);

            factoryManager.context().removeInstance(this.getName(), instance);
        }

        return instances;
    }

    private boolean canAssign(Class<?> targetType, Object value) {
        return value == null || targetType.isAssignableFrom(value.getClass())
                || ConvertUtils.lookup(value.getClass(), targetType) != null;
    }

    private void setBeanProperty(T instance, String property, Object value) {
        try {

            Object targetValue = null;

            PropertyDescriptor targetPropertyDescriptor = PropertyUtils.getPropertyDescriptor(instance, property);
            if (targetPropertyDescriptor == null) {
                throw new RuntimeException(String.format("Could not find property %s in instance %s", property, instance.toString()));
            }

            if (canAssign(targetPropertyDescriptor.getPropertyType(), value)) {
                targetValue = value;
            } else {
                if (value instanceof Collection) {
                    Collection<?> valueCollection = (Collection<?>) value;

                    if (!valueCollection.isEmpty()) {
                        Object first = valueCollection.iterator().next();

                        if (canAssign(targetPropertyDescriptor.getPropertyType(), first)) {
                            targetValue = first;
                        } else {
                            if (targetPropertyDescriptor.getPropertyType().isAssignableFrom(Set.class)) {
                                targetValue = new HashSet<Object>(valueCollection);
                            } else if (targetPropertyDescriptor.getPropertyType().isArray()) {
                                targetValue = valueCollection.toArray(new Object[0]);
                            }
                        }
                    }
                }
            }

            BeanUtils.setProperty(instance, property, targetValue);

        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public String toString() {
        return "Factory [name=" + name + ", fullQualifiedClassName=" + fullQualifiedClassName + ", attributes="
                + attributes + "]";
    }
}
