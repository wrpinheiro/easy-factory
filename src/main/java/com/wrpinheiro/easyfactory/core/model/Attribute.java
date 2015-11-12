package com.wrpinheiro.easyfactory.core.model;

/**
 * This class represents an attribute of the factory.
 *
 * @author Wellington Pinheiro <wellington.pinheiro@gmail.com>
 * @param <T> the formal attribute type
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
}
