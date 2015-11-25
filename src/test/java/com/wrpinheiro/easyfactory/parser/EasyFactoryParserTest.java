package com.wrpinheiro.easyfactory.parser;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.junit.Test;

import java.io.InputStream;

import static org.junit.Assert.assertEquals;

public class EasyFactoryParserTest {

    private EasyFactoryParser parser(String factoryFile) {
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

    @Test
    public void mustParseAFactoryWithoutAttributes() throws Exception {
        EasyFactoryParser parser = this.parser("empty_user.ef");

        ParseTree tree = parser.factoryDecl();

        assertEquals("(factoryDecl factory empty_user , (classDecl class (qualifiedName br . com . easyfactory . model . User)) attributeListDecl end)", tree.toStringTree(parser));
    }

    @Test
    public void mustParseAFactoryWithSimpleAttributes() throws Exception {
        EasyFactoryParser parser = this.parser("simple_user.ef");

        ParseTree tree = parser.factoryDecl();

        assertEquals("(factoryDecl factory simple_user , (classDecl class (qualifiedName br . com . easyfactory . model . User)) (attributeListDecl (attributeDecl id : (literal 1234)) (attributeDecl nickname : (literal \"joseph\")) (attributeDecl email : (literal \"joseph@josephs.com\")) (attributeDecl name : (literal \"Joseph Climber\")) (attributeDecl address : (literal \"an address\"))) end)", tree.toStringTree(parser));
    }

}
