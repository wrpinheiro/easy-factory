package com.thecodeinside.easyfactory.core;

import com.thecodeinside.easyfactory.FactoryReference;

/**
 * A factory's attribute.
 *
 * @param <T> the attribute type
 * 
 * @author Wellington Pinheiro <wellington.pinheiro@gmail.com>
 */
public class Attribute<T> {
    private String id;
    private T value;

    public String getId() {
        return this.id;
    }

    public T getValue() {
        return this.value;
    }

    public Attribute() {}

    public Attribute(String id, T value) {
        this.id = id;
        this.value = value;
    }
    
    @Override
    public String toString() {
        return "Attribute [id=" + id + ", value=" + value + "]";
    }

    public boolean isReference() {
        return value instanceof FactoryReference;
    }
    
    public boolean isNotReference() {
        return !isReference();
    }
}
