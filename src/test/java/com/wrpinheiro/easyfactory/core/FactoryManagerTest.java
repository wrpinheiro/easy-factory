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
            it("must have only one factory instance", () -> {
                FactoryManager fm1 = FactoryManager.instance();
                FactoryManager fm2 = FactoryManager.instance();

                expect(fm1).toEqual(fm2);
            });
            
            it("must load a factory with its references", () -> {
                Factory<User> userFactory = FactoryManager.getFactory("user_with_address_relation");
                
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
                Factory<User> userFactory = FactoryManager.getFactory("simple_user");

                expect(userFactory).toBeNotNull();
                expect(userFactory.getName()).toEqual("simple_user");
            });
        });
        
        describe(".build", () -> {
            it("must build an instance from simple_user", () -> {
                User user = FactoryManager.build("simple_user");
                    
                expect(user).toBeNotNull();
                expect(user.getId()).toEqual(Integer.valueOf(1234));
                expect(user.getNickname()).toEqual("john.doe");
                expect(user.getEmail()).toEqual("john.doe@doe.com");
                expect(user.getName()).toEqual("John Doe");
            });
        });
    }
}
