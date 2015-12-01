package com.wrpinheiro.easyfactory.core.model;

import static com.mscharhag.oleaster.matcher.Matchers.expect;
import static com.mscharhag.oleaster.runner.StaticRunnerSupport.describe;
import static com.mscharhag.oleaster.runner.StaticRunnerSupport.it;

import org.junit.runner.RunWith;

import com.mscharhag.oleaster.runner.OleasterRunner;
import com.wrpinheiro.easyfactory.core.FactoryManager;

@RunWith(OleasterRunner.class)
public class FactoryTest {{
    describe("#build", () -> {
        it("must create an User instance", () -> {
            Factory<User> userFactory = new Factory<>();
            userFactory.setName("simple_user");
            userFactory.setFullQualifiedClassName(User.class.getName());
            userFactory.addAttribute(new Attribute<Integer>("id", 1234));
            userFactory.addAttribute(new Attribute<String>("nickname", "john.doe"));
            userFactory.addAttribute(new Attribute<String>("email", "john.doe@doe.com"));
            userFactory.addAttribute(new Attribute<String>("name", "John Doe"));

            User user = userFactory.build();

            expect(user).toBeNotNull();
            expect(user.getId()).toEqual(1234);
            expect(user.getNickname()).toEqual("john.doe");
            expect(user.getEmail()).toEqual("john.doe@doe.com");
            expect(user.getName()).toEqual("John Doe");
        });
    });
}}

class FactoryTestIgnored {{
    describe("#load", () -> {
        it("must load an user instance from file", () -> {
            Factory<User> userFactory = FactoryManager.load("simple_user");

            User user = userFactory.build();

            expect(user).toBeNotNull();
            expect(user.getId()).toEqual(1234);
            expect(user.getNickname()).toEqual("john.doe");
            expect(user.getEmail()).toEqual("john.doe@doe.com");
            expect(user.getName()).toEqual("John Doe");
        });
    });
}}

