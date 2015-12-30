package com.thecodeinside.easyfactory.parser.impl;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import com.thecodeinside.easyfactory.FactoryReference;
import com.thecodeinside.easyfactory.core.Attribute;
import com.thecodeinside.easyfactory.core.Factory;
import com.thecodeinside.easyfactory.core.FactoryManager;
import com.thecodeinside.easyfactory.parser.EasyFactoryBaseListener;
import com.thecodeinside.easyfactory.parser.EasyFactoryParser;
import com.thecodeinside.easyfactory.parser.EasyFactoryParser.ClassDeclContext;

public class EasyFactoryListenerImpl extends EasyFactoryBaseListener {
    private List<Factory<?>> factories;
    private Factory<?> factory;

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
        if (ctx.StringLiteral() != null) {
            literal = removeQuotes(ctx.StringLiteral().getText());
        } else if (ctx.IntegerLiteral() != null) {
            literal = Integer.valueOf(ctx.IntegerLiteral().getText());
        } else {
            literal = null;
        }

        return literal;
    }

    @Override
    public void enterLiteralAttributeDecl(EasyFactoryParser.LiteralAttributeDeclContext ctx) {
        factory.addAttribute(new Attribute<Object>(ctx.Identifier().getText(), literalToObject(ctx.literal())));
    }

    @Override
    public void enterBuildFactoryAttributeDecl(EasyFactoryParser.BuildFactoryAttributeDeclContext ctx) {
        factory.addAttribute(new Attribute<Object>(ctx.Identifier().get(0).getText(), 
                new FactoryReference(ctx.Identifier().get(1).getText())));
    }

    @Override
    public void enterArrayAttributeDecl(EasyFactoryParser.ArrayAttributeDeclContext ctx) {
        final List<Object> literals = new LinkedList<>();

        ctx.literalList().literal().forEach(literal -> literals.add(literalToObject(literal)));

        factory.addAttribute(new Attribute<Object>(ctx.Identifier().getText(), literals));
    }
}
