package com.thecodeinside.easyfactory.core;

import static java.util.Collections.unmodifiableMap;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Wellington Pinheiro <wellington.pinheiro@gmail.com>
 */
public final class FactoryManager {

    /**
     * The map of factories
     */
    private Map<String, Factory<?>> factories;

    /**
     * The factory name
     */
    private String name;

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

    public void addFactory(Factory<?> factory) {
        if (safeGetFactories().get(factory.getName()) != null) {
            throw new RuntimeException(String.format("Duplicated factory %s.\nCurrent: %s\nNew: %s", factory.getName(), safeGetFactories().get(factory.getName()), factory));
        }
        safeGetFactories().put(factory.getName(), factory);
    }
    
    public void addFactories(Map<String, Factory<?>> factories) {
        factories.values().forEach(this::addFactory);
    }
    
    public Map<String, Factory<?>> getFactories() {
        return unmodifiableMap(safeGetFactories());
    }

    public String getName() {
        return this.name;
    }

    public <T> T build(String factoryName) {
        Factory<T> factory = getFactory(factoryName);

        return factory.build();
    }

    @SuppressWarnings("unchecked")
    public <T> Factory<T> getFactory(String factoryName) {
        return (Factory<T>) safeGetFactories().get(factoryName);
    }

    @Override
    public String toString() {
        return "FactoryManager [name=" + name + "]";
    }
}
