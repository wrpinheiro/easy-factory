package com.wrpinheiro.easyfactory.core;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeWalker;

import com.wrpinheiro.easyfactory.parser.EasyFactoryLexer;
import com.wrpinheiro.easyfactory.parser.EasyFactoryParser;
import com.wrpinheiro.easyfactory.parser.impl.EasyFactoryListenerImpl;

/**
 * @author Wellington Pinheiro <wellington.pinheiro@gmail.com>
 */
public final class FactoryManager {

    /**
     * The factories files' directory 
     */
    private static final String DEFAULT_FACTORIES_DIR = "factories";

    /**
     * The factory files' extension
     */
    private static final String FACTORY_FILE_EXTENSION = "ef";

    /**
     * The map of factories
     */
    private Map<String, Factory<?>> factories;

    public FactoryManager() {
    }

    private boolean isValidFactoryFile(Path path) {
        return !path.toFile().isDirectory() && path.toFile().getAbsolutePath().endsWith(FACTORY_FILE_EXTENSION);
    }

    private synchronized void loadFactories() {
        try (Stream<Path> pathStream = Files.walk(Paths.get(ClassLoader.getSystemResource(DEFAULT_FACTORIES_DIR).toURI()))) {
            pathStream.filter(this::isValidFactoryFile).map(path -> path.toUri()).forEach(factoryFile -> {
                EasyFactoryParser parser = parse(factoryFile);
                ParseTree tree = parser.factoriesDecl();
                ParseTreeWalker walker = new ParseTreeWalker();

                EasyFactoryListenerImpl listener = new EasyFactoryListenerImpl(new FactoryManager());

                walker.walk(listener, tree);

                addFactories(listener.getFactories());
            });
        } catch (IOException | URISyntaxException ex) {
            ex.printStackTrace();
        }
    }

    private void safeGetFactories() {
        if (factories == null) {
            factories = new HashMap<>();
        }
    }

    private void addFactories(Map<String, Factory<?>> factories) {
        safeGetFactories();
        this.factories.putAll(factories);
    }

    private EasyFactoryParser parse(URI factoryFile) {
        try (InputStream sr = factoryFile.toURL().openStream()) {
            ANTLRInputStream input = new ANTLRInputStream(sr);
            EasyFactoryLexer lexer = new EasyFactoryLexer(input);

            return new EasyFactoryParser(new CommonTokenStream(lexer));
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    private Map<String, Factory<?>> getFactories() {
        if (factories == null) {
            synchronized (this) {
                if (factories == null) {
                    loadFactories();
                }
            }
        }
        return factories;
    }
    
    public void addFactory(Factory<?> factory) {
        safeGetFactories();
        this.factories.put(factory.getName(), factory);
    }

    public <T> T build(String factoryName) {
        Factory<T> factory = getFactory(factoryName);

        return factory.build();
    }

    @SuppressWarnings("unchecked")
    public <T> Factory<T> getFactory(String factoryName) {
        return (Factory<T>) getFactories().get(factoryName);
    }
}
