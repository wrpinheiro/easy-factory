package com.thecodeinside.easyfactory.parser.impl;

import java.util.ArrayList;
import java.util.List;

import com.thecodeinside.easyfactory.FactoryReference;
import com.thecodeinside.easyfactory.core.Attribute;
import com.thecodeinside.easyfactory.core.Factory;
import com.thecodeinside.easyfactory.core.FactoryManager;
import com.thecodeinside.easyfactory.parser.EasyFactoryBaseListener;
import com.thecodeinside.easyfactory.parser.EasyFactoryParser;
import com.thecodeinside.easyfactory.parser.EasyFactoryParser.AttributeDeclContext;
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

    @Override
    public void exitAttributeDecl(AttributeDeclContext ctx) {
        Attribute<?> attribute = new Attribute<Object>(ctx.Identifier().getText(), literal);
        factory.addAttribute(attribute);
    }

    @Override
    public void enterLiteral(EasyFactoryParser.LiteralContext ctx) {
        if (ctx.StringLiteral() != null) {
            literal = removeQuotes(ctx.StringLiteral().getText());
        } else if (ctx.IntegerLiteral() != null) {
            literal = Integer.valueOf(ctx.IntegerLiteral().getText());
        } else {
            literal = null;
        }
    }

    @Override
    public void enterBuildFactoryDecl(EasyFactoryParser.BuildFactoryDeclContext ctx) {
        literal = new FactoryReference(ctx.Identifier().getText());
    }
}
