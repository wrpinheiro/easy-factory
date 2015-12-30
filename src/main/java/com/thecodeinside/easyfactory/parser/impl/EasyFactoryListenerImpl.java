package com.thecodeinside.easyfactory.parser.impl;

import java.util.ArrayList;
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

    // private void addLiteral(Object literal) {
    // if (this.literals == null) {
    // this.literals = new LinkedList<>();
    // }
    //
    // this.literals.offer(literal);
    // }

    @Override
    public void enterLiteralAttributeDecl(EasyFactoryParser.LiteralAttributeDeclContext ctx) {
        if (ctx.literal().StringLiteral() != null) {
            literal = removeQuotes(ctx.literal().StringLiteral().getText());
        } else if (ctx.literal().IntegerLiteral() != null) {
            literal = Integer.valueOf(ctx.literal().IntegerLiteral().getText());
        } else {
            literal = null;
        }

        factory.addAttribute(new Attribute<Object>(ctx.Identifier().getText(), literal));
    }

    @Override
    public void enterBuildFactoryAttributeDecl(EasyFactoryParser.BuildFactoryAttributeDeclContext ctx) {
        factory.addAttribute(new Attribute<Object>(ctx.Identifier().get(0).getText(), 
                new FactoryReference(ctx.Identifier().get(1).getText())));
    }
}
