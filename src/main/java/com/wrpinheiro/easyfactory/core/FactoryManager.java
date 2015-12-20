package com.wrpinheiro.easyfactory.core;

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

    public static final FactoryManager DEFAULT_FACTORY_MANAGER = new FactoryManager("DEFAULT");

    /**
     * The factory name
     */
    private String name;

    public FactoryManager(String name) {
        this.name = name;
    }

    private Map<String, Factory<?>> safeGetFactories() {
        if (factories == null) {
            factories = new HashMap<>();
        }

        return factories;
    }

    public void addFactories(Map<String, Factory<?>> factories) {
        safeGetFactories().putAll(factories);
    }

    private Map<String, Factory<?>> getFactories() {
        return unmodifiableMap(this.factories);
    }

    public String getName() {
        return this.name;
    }

    public void addFactory(Factory<?> factory) {
        safeGetFactories().put(factory.getName(), factory);
    }

    public <T> T build(String factoryName) {
        Factory<T> factory = getFactory(factoryName);

        return factory.build();
    }

    @SuppressWarnings("unchecked")
    public <T> Factory<T> getFactory(String factoryName) {
        return (Factory<T>) getFactories().get(factoryName);
    }

    @Override
    public String toString() {
        return "FactoryManager [name=" + name + "]";
    }
}
