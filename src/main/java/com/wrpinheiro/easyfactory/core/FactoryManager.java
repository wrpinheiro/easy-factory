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

import com.wrpinheiro.easyfactory.core.model.Factory;
import com.wrpinheiro.easyfactory.parser.EasyFactoryLexer;
import com.wrpinheiro.easyfactory.parser.EasyFactoryParser;
import com.wrpinheiro.easyfactory.parser.impl.EasyFactoryListenerImpl;

/**
 * @author Wellington Pinheiro <wellington.pinheiro@gmail.com>
 */
public class FactoryManager {

    private static final String DEFAULT_FACTORIES_DIR = "factories";
    private static final String FACTORY_FILE_EXTENSION = "ef";

    private Map<String, Factory<?>> factories = new HashMap<>();

    public FactoryManager() {
        loadFactories();
    }

    private boolean isValidFactoryFile(Path path) {
        return !path.toFile().isDirectory() && path.toFile().getAbsolutePath().endsWith(FACTORY_FILE_EXTENSION);
    }

    private void loadFactories() {
        try (Stream<Path> pathStream = Files.walk(Paths.get(ClassLoader.getSystemResource(DEFAULT_FACTORIES_DIR).toURI()))) {
            pathStream.filter(this::isValidFactoryFile).map(path -> path.toUri()).forEach(factoryFile -> {
                EasyFactoryParser parser = parse(factoryFile);
                ParseTree tree = parser.factoriesDecl();
                ParseTreeWalker walker = new ParseTreeWalker();

                EasyFactoryListenerImpl listener = new EasyFactoryListenerImpl();

                walker.walk(listener, tree);
                
                this.factories.putAll(listener.getFactories());
            });
        } catch (IOException | URISyntaxException ex) {
            ex.printStackTrace();
        }
    }

    public static <T> Factory<T> load(String factoryName) {
        return null;
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

    public Map<String, Factory<?>> getFactories() {
        return factories;
    }
}
