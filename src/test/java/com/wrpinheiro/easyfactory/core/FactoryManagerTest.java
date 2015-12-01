package com.wrpinheiro.easyfactory.core;

import static com.mscharhag.oleaster.matcher.Matchers.expect;
import static com.mscharhag.oleaster.runner.StaticRunnerSupport.describe;
import static com.mscharhag.oleaster.runner.StaticRunnerSupport.it;

import java.util.Map;

import com.wrpinheiro.easyfactory.core.model.Factory;

//@RunWith(OleasterRunner.class)
public class FactoryManagerTest {{
    describe("", () -> {
        it ("", () -> {
          FactoryManager factoryManager = new FactoryManager();
         Map<String, Factory<?>> factories = factoryManager.getFactories();

          expect(factories).toBeNotNull();
          expect(factories.size()).toBeGreaterThan(0);
        });
    });
}}
