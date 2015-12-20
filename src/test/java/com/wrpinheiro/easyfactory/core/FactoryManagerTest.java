package com.wrpinheiro.easyfactory.core;

import static com.mscharhag.oleaster.matcher.Matchers.expect;
import static com.mscharhag.oleaster.runner.StaticRunnerSupport.describe;
import static com.mscharhag.oleaster.runner.StaticRunnerSupport.it;

import org.junit.runner.RunWith;

import com.mscharhag.oleaster.runner.OleasterRunner;
import com.wrpinheiro.easyfactory.FactoryReference;
import com.wrpinheiro.easyfactory.core.model.User;

@RunWith(OleasterRunner.class)
public class FactoryManagerTest {
    {
        describe(".instance", () -> {
            it("must load a factory with its references", () -> {
                Factory<User> userFactory = new FactoryManager().getFactory("user_with_address_relation");

                expect(userFactory).toBeNotNull();

                Attribute<?> attribute = userFactory.getAttributes().get("address");

                expect(attribute).toBeNotNull();
                expect(attribute.getValue().getClass()).toEqual(FactoryReference.class);

                FactoryReference factoryReference = (FactoryReference) attribute.getValue();
                expect(factoryReference).toBeNotNull();
                expect(factoryReference.getReference()).equals("address");
            });
        });

        describe(".getFactory", () -> {
            it("must get a factory for simple_user", () -> {
                Factory<User> userFactory = new FactoryManager().getFactory("simple_user");

                expect(userFactory).toBeNotNull();
                expect(userFactory.getName()).toEqual("simple_user");
            });
        });

        describe(".build", () -> {
            it("must build an instance from simple_user", () -> {
                User user = new FactoryManager().build("simple_user");

                expect(user).toBeNotNull();
                expect(user.getId()).toEqual(Integer.valueOf(1234));
                expect(user.getNickname()).toEqual("john.doe");
                expect(user.getEmail()).toEqual("john.doe@doe.com");
                expect(user.getName()).toEqual("John Doe");
            });
            
            it("must build a factory with reference", () -> {
                User user = new FactoryManager().build("user_with_address_relation");
                
                expect(user).toBeNotNull();
                expect(user.getId()).toEqual(1234);
                expect(user.getAddress()).toBeNotNull();
                expect(user.getAddress().getStreet()).toEqual("Mountain St");
            });
        });

        describe("#addFactory", () -> {
            FactoryManager factoryManager = new FactoryManager();
            
            it("must add a factory to factory manager", () -> {
                Factory<User> userFactory = new Factory<User>(factoryManager, "user");
                userFactory.setFullQualifiedClassName(User.class.getName());
                userFactory.addAttribute(new Attribute<Integer>("id", 15423));

                FactoryManager fm = new FactoryManager();

                fm.addFactory(userFactory);
                Factory<User> otherUserFactory = fm.getFactory("user");

                expect(otherUserFactory).toBeNotNull();
                expect(otherUserFactory.getName()).toEqual("user");
                expect(otherUserFactory.setFullQualifiedClassName()).toEqual(User.class.getName());
                expect(otherUserFactory.getAttributes().get("id").getValue()).toEqual(15423);
            });
        });
    }
}
