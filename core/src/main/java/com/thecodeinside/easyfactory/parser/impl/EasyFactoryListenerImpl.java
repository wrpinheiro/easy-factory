package com.thecodeinside.easyfactory.parser.impl;

import com.thecodeinside.easyfactory.FactoryReference;
import com.thecodeinside.easyfactory.core.Attribute;
import com.thecodeinside.easyfactory.core.Factory;
import com.thecodeinside.easyfactory.core.FactoryManager;
import com.thecodeinside.easyfactory.parser.EasyFactoryBaseListener;
import com.thecodeinside.easyfactory.parser.EasyFactoryParser;
import com.thecodeinside.easyfactory.parser.EasyFactoryParser.ClassDeclContext;
import org.antlr.v4.runtime.tree.TerminalNode;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * An ANTRL implementation of listener. This class is used to create instances of factories based on the AST
 * generated for each factory defined in the <code>ef</code> files.
 * 
 * @author Wellington Pinheiro <wellington.pinheiro@gmail.com>
 */
public class EasyFactoryListenerImpl extends EasyFactoryBaseListener {
    private List<Factory<?>> factories;
    private Factory<?> factory;

    private String attributeIdentifier;
    private Object literal;
    private FactoryManager factoryManager;

    public EasyFactoryListenerImpl(FactoryManager factoryManager) {
        this.factoryManager = factoryManager;
    }

    public List<Factory<?>> getFactories() {
        return factories;
    }

    private Object removeQuotes(String text) {
        return text.length() > 0 ? text.subSequence(1, text.length() - 1) : "";
    }

    @Override
    public void enterFactoriesDecl(EasyFactoryParser.FactoriesDeclContext ctx) {
        factories = new ArrayList<Factory<?>>();
    }

    @Override
    public void enterFactoryDecl(EasyFactoryParser.FactoryDeclContext ctx) {
        String factoryName = ctx.Identifier().getText();

        factory = new Factory<Object>(factoryManager, factoryName);
        factories.add(factory);
    }

    @Override
    public void enterClassDecl(ClassDeclContext ctx) {
        factory.setFullQualifiedClassName(ctx.qualifiedName().getText());
    }

    private Object literalToObject(EasyFactoryParser.LiteralContext ctx) {
        if (ctx.stringLiteral() != null) {
            literal = removeQuotes(ctx.stringLiteral().StringLiteral().getText());
        } else if (ctx.integerLiteral() != null) {
            literal = Integer.valueOf(ctx.integerLiteral().IntegerLiteral().getText());
        } else {
            literal = null;
        }

        return literal;
    }

    @Override
    public void enterAttributeDecl(EasyFactoryParser.AttributeDeclContext ctx) {
        attributeIdentifier = ctx.Identifier().getText();
    }

    @Override
    public void enterLiteral(EasyFactoryParser.LiteralContext ctx) {
        factory.addAttribute(new Attribute<Object>(attributeIdentifier, literalToObject(ctx)));

        super.enterLiteral(ctx);
    }

    @Override
    public void enterBuildFactoryAttributeDecl(EasyFactoryParser.BuildFactoryAttributeDeclContext ctx) {
        String[] factoriesReferences = ctx.identifierListDecl().Identifier().stream().map(TerminalNode::getText).collect(Collectors.toList()).toArray(new String[0]);

        factory.addAttribute(new Attribute<Object>(attributeIdentifier, new FactoryReference(factoryManager, factoriesReferences)));
    }

//    @Override
//    public void enterArrayAttributeDecl(EasyFactoryParser.ArrayAttributeDeclContext ctx) {
//        final List<Object> literals = new ArrayList<>();
//
//        ctx.literalListDecl().literal().forEach(literal -> literals.add(literalToObject(literal)));
//
//        factory.addAttribute(new Attribute<Object>(ctx.Identifier().getText(), literals));
//    }
}
