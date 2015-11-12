package com.wrpinheiro.easyfactory.core;

import com.wrpinheiro.easyfactory.parser.EasyFactoryLexer;
import com.wrpinheiro.easyfactory.parser.EasyFactoryParser;
import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;

import java.io.InputStream;

/**
 * @author Wellington Pinheiro <wellington.pinheiro@walmart.com>
 */
public class FactoryLoader {
    public EasyFactoryParser parser(String factoryFile) {
        try {
            InputStream sr = getClass().getClassLoader().getResourceAsStream(factoryFile);
            ANTLRInputStream input = new ANTLRInputStream(sr);
            EasyFactoryLexer lexer = new EasyFactoryLexer(input);
            CommonTokenStream tokens = new CommonTokenStream(lexer);

            return new EasyFactoryParser(tokens);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }
}
