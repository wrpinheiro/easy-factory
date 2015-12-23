package com.wrpinheiro.easyfactory.loader;

import static com.mscharhag.oleaster.matcher.Matchers.expect;
import static com.mscharhag.oleaster.runner.StaticRunnerSupport.describe;
import static com.mscharhag.oleaster.runner.StaticRunnerSupport.it;

import org.junit.runner.RunWith;

import com.mscharhag.oleaster.runner.OleasterRunner;
import com.wrpinheiro.easyfactory.FactoryReference;
import com.wrpinheiro.easyfactory.core.Attribute;
import com.wrpinheiro.easyfactory.core.Factory;
import com.wrpinheiro.easyfactory.core.FactoryManager;
import com.wrpinheiro.easyfactory.core.model.User;

@RunWith(OleasterRunner.class)
public class FileSystemLoaderTest {
    {
        FileSystemLoader fsLoader = new FileSystemLoader();
        fsLoader.loadFactories();
        
        FactoryManager factoryManager = fsLoader.factoryManager();

        describe("#loadFactories", () -> {
            it("must load a factory with its references", () -> {
                Factory<User> userFactory = factoryManager.getFactory("user_with_address_relation");

                expect(userFactory).toBeNotNull();

                Attribute<?> attribute = userFactory.getAttributes().get("address");

                expect(attribute).toBeNotNull();
                expect(attribute.getValue().getClass()).toEqual(FactoryReference.class);

                FactoryReference factoryReference = (FactoryReference) attribute.getValue();
                expect(factoryReference).toBeNotNull();
                expect(factoryReference.getReference()).equals("address");
            });
            
            it("must get a factory for simple_user", () -> {
                Factory<User> userFactory = factoryManager.getFactory("simple_user");

                expect(userFactory).toBeNotNull();
                expect(userFactory.getName()).toEqual("simple_user");
            });
            
            it("must build an instance from simple_user", () -> {
                User user = factoryManager.build("simple_user");

                expect(user).toBeNotNull();
                expect(user.getId()).toEqual(Integer.valueOf(10203040));
                expect(user.getNickname()).toEqual("john.doe");
                expect(user.getEmail()).toEqual("john.doe@doe.com");
                expect(user.getName()).toEqual("John Doe");
            });
            
            it("must build a factory with reference", () -> {
                FileSystemLoader newFsLoader = new FileSystemLoader();
                newFsLoader.loadFactories();
                
                User user = newFsLoader.factoryManager().build("user_with_address_relation");
                
                expect(user).toBeNotNull();
                expect(user.getId()).toEqual(31318080);
                expect(user.getAddress()).toBeNotNull();
                expect(user.getAddress().getStreet()).toEqual("Mountain St");
            });
        });
    }
}
