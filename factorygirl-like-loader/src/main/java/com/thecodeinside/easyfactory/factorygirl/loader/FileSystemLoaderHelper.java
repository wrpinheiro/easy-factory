package com.thecodeinside.easyfactory.factorygirl.loader;

import com.thecodeinside.easyfactory.core.FactoryManager;

/**
 * Utility class that helps to load factories from file. It guarantees that only one Factory Manager
 * will be created even when multiple tests simultaneously ask for an instance of Factory Manager.
 *  
 * @author Wellington Pinheiro <wellington.pinheiro@gmail.com>
 */
public final class FileSystemLoaderHelper {
    private FileSystemLoader fsLoader;

    private static FileSystemLoaderHelper instance;

    private FileSystemLoaderHelper() {
        fsLoader = new FileSystemLoader();
    }

    public static synchronized FactoryManager fsFactoryManager() {
        if (instance == null) {
            instance = new FileSystemLoaderHelper();
        }

        instance.fsLoader.loadFactories();

        return instance.fsLoader.factoryManager();
    }
}
