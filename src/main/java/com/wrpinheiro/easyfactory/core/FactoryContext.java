package com.wrpinheiro.easyfactory.core;

import java.util.HashMap;
import java.util.Map;

public final class FactoryContext {
    private Map<String, Object> instances;

    private static final ThreadLocal<FactoryContext> factoryContextTl = new ThreadLocal<FactoryContext>() {
        @Override
        protected FactoryContext initialValue() {
            return new FactoryContext();
        }
    };

    private FactoryContext() {
        instances = new HashMap<>();
    }

    public static final FactoryContext context() {
        return factoryContextTl.get();
    }

    public void addInstance(String factoryName, Object o) {
        this.instances.put(factoryName, o);
    }
    
    public Object instance(String factoryName) {
        return instances.get(factoryName);
    }
}
