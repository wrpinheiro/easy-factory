package com.wrpinheiro.easyfactory.parser.impl;

import java.util.HashMap;
import java.util.Map;

import com.wrpinheiro.easyfactory.core.model.Attribute;
import com.wrpinheiro.easyfactory.core.model.Factory;
import com.wrpinheiro.easyfactory.parser.EasyFactoryBaseListener;
import com.wrpinheiro.easyfactory.parser.EasyFactoryParser;
import com.wrpinheiro.easyfactory.parser.EasyFactoryParser.AttributeDeclContext;
import com.wrpinheiro.easyfactory.parser.EasyFactoryParser.ClassDeclContext;

public class EasyFactoryListenerImpl extends EasyFactoryBaseListener {
    private Map<String, Factory<?>> factories;
    private Factory<?> factory;
    
    private Object literal;

    public Map<String, Factory<?>> getFactories() {
        return factories;
    }

    @Override
    public void enterFactoriesDecl(EasyFactoryParser.FactoriesDeclContext ctx) {
        factories = new HashMap<String, Factory<?>>();
    }

    @Override
    public void enterFactoryDecl(EasyFactoryParser.FactoryDeclContext ctx) {
        factory = new Factory<Object>(ctx.Identifier().getText());
        factories.put(ctx.Identifier().getText(), factory);
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
    
    public void enterLiteral(EasyFactoryParser.LiteralContext ctx) {
        if (ctx.StringLiteral() != null) {
            literal = removeQuotes(ctx.StringLiteral().getText());
        } else if (ctx.IntegerLiteral() != null) {
            literal = Integer.valueOf(ctx.IntegerLiteral().getText());
        } else {
            literal = null;
        }
    }

    private Object removeQuotes(String text) {
        return text.length() > 0 ? text.subSequence(1, text.length() - 1) : "";
    }
}
