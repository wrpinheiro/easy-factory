package com.thecodeinside.easyfactory.core;

/**
 * A factory's attribute.
 * 
 * @author Wellington Pinheiro <wellington.pinheiro@gmail.com>
 *
 * @param <T> type of the attribute
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

    public Attribute() {
    }

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
