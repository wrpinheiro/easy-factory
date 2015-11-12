package com.wrpinheiro.easyfactory.core;

import com.wrpinheiro.easyfactory.core.model.Factory;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Wellington Pinheiro <wellington.pinheiro@gmail.com>
 */
public class FactoryManager {
    private String factoryFilesPath;
    private Map<String, Factory> factories;

    public FactoryManager(String factoryFilesPath) {
        this.factories = new HashMap<>();
    }

    public static <T> T load(String factoryName) {
        return null;
    }
}
