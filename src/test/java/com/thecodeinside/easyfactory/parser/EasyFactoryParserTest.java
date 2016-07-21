package com.thecodeinside.easyfactory.parser;

import com.thecodeinside.easyfactory.parser.EasyFactoryParser.*;
import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.TerminalNode;
import org.junit.Test;

import java.io.InputStream;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class EasyFactoryParserTest {

    private EasyFactoryParser parser(String factoryFile) {
        try (InputStream sr = getClass().getClassLoader().getResourceAsStream(factoryFile)) {
            ANTLRInputStream input = new ANTLRInputStream(sr);
            EasyFactoryLexer lexer = new EasyFactoryLexer(input);
            CommonTokenStream tokens = new CommonTokenStream(lexer);

            return new EasyFactoryParser(tokens);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    private void testLiteral(String expect, String actual) {
        assertEquals("\"" + expect + "\"", actual);
    }

    private void testLiteral(Integer expected, String actual) {
        assertEquals(expected.toString(), actual);
    }

    private void testLiteralAttribute(AttributeDeclContext ctx, String id, String value) {
        assertEquals(id, ctx.Identifier().getText());
        testLiteral(value, ctx.literal().stringLiteral().getText());
    }

    private void testLiteralAttribute(AttributeDeclContext ctx, String id, Integer value) {
        assertEquals(id, ctx.Identifier().getText());
        testLiteral(value, ctx.literal().integerLiteral().getText());
    }

//    private void testArrayAttribute(AttributeDeclContext ctx, String id, String[] values) {
//        assertEquals(id, ctx.Identifier().getText());
//        assertEquals(values.length, ctx.literalListDecl().literal().size());
//
//        for (int i = 0; i < values.length; i++) {
//            testLiteral(values[i], ctx.literalListDecl().literal(i).StringLiteral().getText());
//        }
//    }

    @Test
    public void must_parse_a_factory_without_attributes() {
        EasyFactoryParser parser = this.parser("factories/empty_user.ef");

        FactoryDeclContext factoryDeclContext = parser.factoryDecl();
        assertEquals("empty_user", factoryDeclContext.Identifier().getText());

        ClassDeclContext classDeclContext = factoryDeclContext.classDecl();
        List<TerminalNode> classDeclIdentifiers = classDeclContext.qualifiedName().Identifier();
        assertEquals("User", classDeclIdentifiers.get(classDeclIdentifiers.size() - 1).getText());

        assertEquals(0, factoryDeclContext.attributeListDecl().attributeDecl().size());
    }

    @Test
    public void must_parse_a_factory_with_simple_attributes() {
        EasyFactoryParser parser = this.parser("factories/simple_user.ef");

        FactoryDeclContext factoryDeclContext = parser.factoryDecl();
        assertEquals("simple_user", factoryDeclContext.Identifier().getText());

        assertEquals(4, factoryDeclContext.attributeListDecl().attributeDecl().size());

        AttributeDeclContext id = factoryDeclContext.attributeListDecl().attributeDecl(0);
        AttributeDeclContext nickname = factoryDeclContext.attributeListDecl().attributeDecl(1);
        AttributeDeclContext email = factoryDeclContext.attributeListDecl().attributeDecl(2);
        AttributeDeclContext name = factoryDeclContext.attributeListDecl().attributeDecl(3);

        testLiteralAttribute(id, "id", new Integer(10203040));
        testLiteralAttribute(nickname, "nickname", "john.doe");
        testLiteralAttribute(email, "email", "john.doe@doe.com");
        testLiteralAttribute(name, "name", "John Doe");
    }

    @Test
    public void must_parse_a_factory_with_relations() {
        EasyFactoryParser parser = this.parser("factories/user_with_address.ef");

        FactoriesDeclContext factoriesDeclContext = parser.factoriesDecl();
        assertEquals(2, factoriesDeclContext.factoryDecl().size());

        FactoryDeclContext addressFactory = factoriesDeclContext.factoryDecl(0);
        assertEquals("address", addressFactory.Identifier().getText());

        FactoryDeclContext userFactory = factoriesDeclContext.factoryDecl(1);
        assertEquals("user_with_address_relation", userFactory.Identifier().getText());

        AttributeDeclContext addressReference = userFactory.attributeListDecl().attributeDecl(4);
        BuildFactoryAttributeDeclContext buildFactoryAttributeDeclContext = addressReference.literal().buildFactoryAttributeDecl();

        assertEquals("address", addressReference.Identifier().getText());

        assertEquals(1, buildFactoryAttributeDeclContext.identifierListDecl().Identifier().size());
        assertEquals("address", buildFactoryAttributeDeclContext.identifierListDecl().Identifier(0).getText());
    }

    @Test
    public void mustParseAFactoryWithAnArrayOfString() {
        EasyFactoryParser parser = this.parser("factories/user_with_permissions.ef");

        FactoryDeclContext factoryDeclContext = parser.factoryDecl();
        assertEquals("user_with_permissions", factoryDeclContext.Identifier().getText());

        assertEquals(4, factoryDeclContext.attributeListDecl().attributeDecl().size());

        AttributeDeclContext id = factoryDeclContext.attributeListDecl().attributeDecl(0);
//        ArrayAttributeDeclContext permissions = factoryDeclContext.attributeListDecl().attributeDecl(3).arrayAttributeDecl();

        testLiteralAttribute(id, "id", new Integer(3452904));
//        testArrayAttribute(permissions, "permissions", new String[] { "operations", "remote", "joker" });
    }
}
