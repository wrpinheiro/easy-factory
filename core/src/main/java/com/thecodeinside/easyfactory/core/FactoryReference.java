package com.thecodeinside.easyfactory.core;

import static java.util.stream.Collectors.toList;

import java.util.List;
import java.util.stream.Stream;

import com.thecodeinside.easyfactory.core.FactoryManager;

/**
 * A reference to another factory. This allows relationships between model created by the factories.
 *  
 * @author Wellington Pinheiro <wellington.pinheiro@gmail.com>
 */
public class FactoryReference {

    private FactoryManager factoryManager;
    private String[] references;

    public FactoryReference(FactoryManager factoryManager, String... references) {
        this.factoryManager = factoryManager;
        this.references = references;
    }

    public String[] getReferences() {
        return references;
    }

    public List<Object> loadReferences() {
        List<Object> instances = Stream.of(this.references).map(this::creteOneReference).collect(toList());

        return instances;
    }

    private Object creteOneReference(String referenceName) {
        Object referenceInstance = factoryManager.context().instance(referenceName);
        if (referenceInstance == null) {
            referenceInstance = factoryManager.build(referenceName);
        }

        return referenceInstance;
    }
}
