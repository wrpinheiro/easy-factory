package com.wrpinheiro.easyfactory.core;

import static com.mscharhag.oleaster.matcher.Matchers.expect;
import static com.mscharhag.oleaster.runner.StaticRunnerSupport.describe;
import static com.mscharhag.oleaster.runner.StaticRunnerSupport.it;

import org.junit.runner.RunWith;

import com.mscharhag.oleaster.runner.OleasterRunner;
import com.wrpinheiro.easyfactory.core.Attribute;
import com.wrpinheiro.easyfactory.core.Factory;
import com.wrpinheiro.easyfactory.core.FactoryManager;
import com.wrpinheiro.easyfactory.core.model.User;

@RunWith(OleasterRunner.class)
public class FactoryTest {
    {
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

            it("must create the User instance from factory loaded through FactoryManager", () -> {
                Factory<User> userFactory = FactoryManager.getFactory("simple_user");

                User user = userFactory.build();

                expect(user).toBeNotNull();
                expect(user.getId()).toEqual(1234);
                expect(user.getNickname()).toEqual("john.doe");
                expect(user.getEmail()).toEqual("john.doe@doe.com");
                expect(user.getName()).toEqual("John Doe");
            });
        });
    }
}
