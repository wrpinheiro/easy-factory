package com.thecodeinside.easyfactory;

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
    
    public Object loadReference() {
        String referenceName = getReferences()[0];
        Object referenceInstance = factoryManager.context().instance(referenceName);
        if (referenceInstance == null) {
            referenceInstance = factoryManager.build(referenceName);
        }
        
        return referenceInstance;
    }

}
