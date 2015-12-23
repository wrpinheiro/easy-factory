package com.thecodeinside.easyfactory.core;

import static com.mscharhag.oleaster.matcher.Matchers.expect;
import static com.mscharhag.oleaster.runner.StaticRunnerSupport.describe;
import static com.mscharhag.oleaster.runner.StaticRunnerSupport.it;

import org.junit.runner.RunWith;

import com.mscharhag.oleaster.runner.OleasterRunner;
import com.thecodeinside.easyfactory.core.Attribute;
import com.thecodeinside.easyfactory.core.Factory;
import com.thecodeinside.easyfactory.core.FactoryManager;
import com.thecodeinside.easyfactory.core.model.User;

@RunWith(OleasterRunner.class)
public class FactoryManagerTest {
    {
        describe("#addFactory", () -> {
            it("must add a factory to factory manager", () -> {
                FactoryManager fm = new FactoryManager(this.getClass().getName());

                Factory<User> userFactory = new Factory<User>(fm, "user");
                userFactory.setFullQualifiedClassName(User.class.getName());
                userFactory.addAttribute(new Attribute<Integer>("id", 15423));

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
