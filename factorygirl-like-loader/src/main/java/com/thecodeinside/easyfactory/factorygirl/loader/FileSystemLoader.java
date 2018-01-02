package com.thecodeinside.easyfactory.factorygirl.loader;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeWalker;

import com.thecodeinside.easyfactory.core.FactoryManager;
import com.thecodeinside.easyfactory.parser.EasyFactoryLexer;
import com.thecodeinside.easyfactory.parser.EasyFactoryParser;
import com.thecodeinside.easyfactory.parser.impl.EasyFactoryListenerImpl;

/**
 * Class responsible to load all factories configuration stored in the file system. By default, it will load all
 * files in the directory <code>factories</code> with extension <code>ef</code>.
 * 
 * A new factory manager will be created to store all factories loaded.
 *
 * @author Wellington Pinheiro <wellington.pinheiro@gmail.com>
 */
public class FileSystemLoader {
    /**
     * The factories files' directory 
     */
    private static final String DEFAULT_FACTORIES_DIR = "factories";

    /**
     * The factory files' extension
     */
    private static final String FACTORY_FILE_EXTENSION = "ef";

    private FactoryManager factoryManager;

    public FileSystemLoader() {
        factoryManager = new FactoryManager("FS Loaded Factory Manager");
    }

    private boolean isValidFactoryFile(Path path) {
        return !path.toFile().isDirectory() && path.toFile().getAbsolutePath().endsWith(FACTORY_FILE_EXTENSION);
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

    /**
     * Load all files with extension <code>ef</code> in directory <code>factories</code>.
     */
    public synchronized void loadFactories() {
        try (Stream<Path> pathStream = Files.walk(Paths.get(ClassLoader.getSystemResource(DEFAULT_FACTORIES_DIR).toURI()))) {
            pathStream.filter(this::isValidFactoryFile).map(path -> path.toUri()).forEach(factoryFile -> {
                EasyFactoryParser parser = parse(factoryFile);
                ParseTree tree = parser.factoriesDecl();
                ParseTreeWalker walker = new ParseTreeWalker();

                EasyFactoryListenerImpl listener = new EasyFactoryListenerImpl(factoryManager);

                walker.walk(listener, tree);

                factoryManager.addFactories(listener.getFactories());
            });
        } catch (IOException | URISyntaxException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Return the factory manager created.
     * @return
     */
    public FactoryManager factoryManager() {
        return factoryManager;
    }

}
