package com.thecodeinside.easyfactory.core;

import static java.lang.String.format;

import java.util.HashMap;
import java.util.Map;

/**
 * A helper class used to solve cyclic relantionships between instances.
 * 
 * Suppose the following factories definitions:
 * 
 * factory A
 *   attribute B
 * 
 * factory B
 *   attribute A
 *   
 * If we try to build factory B, then an instance of B without any relationship will be added to the context, then
 * the relationship to A will force the creation of an instance of A. When the relationship from A to B is processed,
 * instead of creating another instance of B, the instance already added to the context will be used and the 
 * relationship will be satisfied and instance A will be complete. At the end, only one instance of A and one of B
 * will be created.
 * 
 * Note the context is defined by Factory Manager and the current thread.
 * 
 * @author Wellington Pinheiro <wellington.pinheiro@gmail.com>
 *
 */
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

    /**
     * Add an instance and the name of the factory responsible to add it to the context.
     * 
     * @param factoryName the name of the factory
     * @param instance the instance created
     */
    public void addInstance(String factoryName, Object instance) {
        if (instance(factoryName) != null) {
            throw new RuntimeException(format("Trying to add duplicate instance in context. \n [Factory Manager] %s \n [Current] %s\n [New] %s", factoryManager, instance(factoryName), instance));
        }
        forceAddInstance(factoryName, instance);
    }

    /**
     * Remove an instance previously added to the context. 
     * 
     * @param factoryName the name of the factory
     * @param instance the instance created
     */
    public void removeInstance(String factoryName, Object instance) {
        Map<String, Object> factoryManagerContext = factoryContextTl.get().get(factoryManager);
        if (factoryManagerContext != null) {
            factoryManagerContext.remove(factoryName);
        }
    }

    private void forceAddInstance(String factoryName, Object o) {
        Map<String, Object> factoryManagerContext = factoryContextTl.get().get(factoryManager);

        if (factoryManagerContext == null) {
            factoryManagerContext = new HashMap<>();
            factoryContextTl.get().put(factoryManager, factoryManagerContext);
        }

        factoryManagerContext.put(factoryName, o);
    }

    /**
     * Return an instance created by factory <code>factoryName</code> stored in this context. Return null if no 
     * instance could be found.
     * 
     * @param factoryName the name of the factory
     * 
     * @return an instance created by the factory defined by <code>factoryName</code>
     */
    public Object instance(String factoryName) {
        if (factoryContextTl.get().containsKey(factoryManager)) {
            return factoryContextTl.get().get(factoryManager).get(factoryName);
        }

        return null;
    }
}
