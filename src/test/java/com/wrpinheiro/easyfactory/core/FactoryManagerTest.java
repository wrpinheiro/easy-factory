package com.wrpinheiro.easyfactory.core;

import static com.mscharhag.oleaster.matcher.Matchers.expect;
import static com.mscharhag.oleaster.runner.StaticRunnerSupport.describe;
import static com.mscharhag.oleaster.runner.StaticRunnerSupport.it;

import java.util.Map;

import org.junit.runner.RunWith;

import com.mscharhag.oleaster.runner.OleasterRunner;
import com.wrpinheiro.easyfactory.core.model.Factory;
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
        });

        describe("#getFactories", () -> {
            it("must load three example factories", () -> {
                FactoryManager factoryManager = FactoryManager.instance();
                Map<String, Factory<?>> factories = factoryManager.getFactories();

                expect(factories).toBeNotNull();
                expect(factories.size()).toEqual(3);
            });
        });
        
        describe(".getFactory", () -> {
            it("must get a factory for simple_user", () -> {
                Factory<User> userFactory = FactoryManager.getFactory("simple_user");

                expect(userFactory).toBeNotNull();
                expect(userFactory.getName()).toEqual("simple_user");
            });
        });
    }
}
