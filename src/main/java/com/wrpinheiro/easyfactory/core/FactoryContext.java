package com.wrpinheiro.easyfactory.core;

import static java.lang.String.format;
import static java.util.Optional.ofNullable;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public final class FactoryContext {
    private FactoryManager factoryManager;

    private static final ThreadLocal<Map<FactoryManager, Map<String, Object>>> factoryContextTl = new ThreadLocal<Map<FactoryManager, Map<String, Object>>>() {
        @Override
        protected Map<FactoryManager, Map<String, Object>> initialValue() {
            return new HashMap<>();
        }
    };

    public FactoryContext(FactoryManager factoryManager) {
        this.factoryManager = factoryManager;
    }

    public void addInstance(String factoryName, Object o) {
        if (instance(factoryName) != null) {
            throw new RuntimeException(format("Trying to add duplicate instance in context. \n [Factory Manager] %s \n [Current] %s\n [New] %s", factoryManager, instance(factoryName), o));
        }
        forceAddInstance(factoryName, o);
    }

    private void forceAddInstance(String factoryName, Object o) {
        Map<String, Object> factoryManagerContext = factoryContextTl.get().get(factoryManager);

        if (factoryManagerContext == null) {
            factoryManagerContext = new HashMap<>();
            factoryContextTl.get().put(factoryManager, factoryManagerContext);
        }

        factoryManagerContext.put(factoryName, o);
    }

    public Object instance(String factoryName) {
        if (factoryContextTl.get().containsKey(factoryManager)) {
            return factoryContextTl.get().get(factoryManager).get(factoryName);
        }

        return null;
    }
}
