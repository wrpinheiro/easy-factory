package com.thecodeinside.easyfactory.core;

import static java.util.Collections.unmodifiableMap;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Class responsible to group factories. An application can have many factory managers and they are each one isolated.
 * 
 * The factory manager is specially important to solve relationships between factories. When a relationship to Fr is
 * found in a factory F, the factory manager of F will be used to find the factory for Fr. Due to this, all factories
 * that relate in any form must be available in the same Factory Manager.
 * 
 * @author Wellington Pinheiro <wellington.pinheiro@gmail.com>
 */
public final class FactoryManager {

    /**
     * The map of factories
     */
    private Map<String, Factory<?>> factories;

    /**
     * The factory name.
     */
    private String name;

    /**
     * The context used to solve cyclic relationships.
     */
    private FactoryContext factoryContext;

    public FactoryManager(String name) {
        this.name = name;

        this.factoryContext = new FactoryContext(this);
    }

    private Map<String, Factory<?>> safeGetFactories() {
        if (factories == null) {
            factories = new HashMap<>();
        }

        return factories;
    }

    public FactoryContext context() {
        return factoryContext;
    }

    /**
     * Add a factory to this factory manager.
     * 
     * @param factory the factory to be added.
     */
    public void addFactory(Factory<?> factory) {
        if (safeGetFactories().get(factory.getName()) != null) {
            throw new RuntimeException(String.format("Duplicated factory %s.\nCurrent: %s\nNew: %s", factory.getName(), safeGetFactories().get(factory.getName()), factory));
        }
        safeGetFactories().put(factory.getName(), factory);
    }

    /**
     * Add a list of factories.
     * 
     * @param factories a list of factories.
     */
    public void addFactories(List<Factory<?>> factories) {
        factories.forEach(this::addFactory);
    }

    /**
     * Return an unmodifiable map of factories managed by this factory manager.
     * 
     * @return a map of factories.
     */
    public Map<String, Factory<?>> getFactories() {
        return unmodifiableMap(safeGetFactories());
    }

    public String getName() {
        return this.name;
    }

    /**
     * Build an instance defined by the factory with name <code>factoryName</code>
     *
     * @param factoryName the name of the factory.
     * 
     * @return an instance of type T
     */
    public <T> T build(String factoryName) {
        Factory<T> factory = getFactory(factoryName);

        return factory.build();
    }

    /**
     * Return the factory of name <code>factoryName</code> managed by this factory manager.
     * 
     * @param factoryName the name of the factory.
     * 
     * @return a factory instance.
     */
    @SuppressWarnings("unchecked")
    public <T> Factory<T> getFactory(String factoryName) {
        return (Factory<T>) safeGetFactories().get(factoryName);
    }

    @Override
    public String toString() {
        return "FactoryManager [name=" + name + "]";
    }
}
