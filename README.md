# Easy factory

[![Build Status](https://travis-ci.org/wrpinheiro/easy-factory.svg?branch=master)](https://travis-ci.org/wrpinheiro/easy-factory)

## What is Easy factory?

Easy Factory is a great way to create mock data for tests.

The idea of this project is to have something similar of [Thoughtbot's Factory Girl](https://github.com/thoughtbot/factory_girl), a solution widely adopted for Ruby developers, in the Java world.

Below are some examples of factory definition.

```
factory empty_user, class br.com.easyfactory.model.User
end
```

This code above creates a factory with name `empty_user` for `User` class but doesn't set any of its attributes.


```
factory simple_user, class br.com.easyfactory.model.User
  id: 1234
  nickname: "joseph"
  email: "joseph@josephs.com"
  name: "Joseph Climber"
  address: "Any address you might like :-)"
end
```

The `simple_user` factory allows to create instances of `User` and set its properties `id`, `nickname`, `email`, `name`, and `address`

## How to use?

1. Build the `easy-factory` project (see section `How to build` below)
2. Install the `jar` file in your local maven repository: 

```
mvn install:install-file -Dfile=build/libs/easy-factory-0.0.1-SNAPSHOT.jar -DgroupId=com.wrpinheiro.easyfactory \
    -DartifactId=easy-factory -Dversion=0.0.1-SNAPSHOT -Dpackaging=jar
```

3. Add `easy-factory` and its dependencies* to your project `pom.xml`:

```
    <dependency>
        <groupId>commons-beanutils</groupId>
        <artifactId>commons-beanutils</artifactId>
        <version>1.9.2</version>
        <scope>test</scope>
    </dependency>
    <dependency>
        <groupId>org.antlr</groupId>
        <artifactId>antlr4-runtime</artifactId>
        <version>4.5.1-1</version>
        <scope>test</scope>
    </dependency>
    <dependency>
        <groupId>com.wrpinheiro.easyfactory</groupId>
        <artifactId>easy-factory</artifactId>
        <version>0.0.1-SNAPSHOT</version>
        <scope>test</scope>
    </dependency>
```

4. Create a `factories` directory in the `src/test/resources/factories` directory and add the factory files inside of it.

5. Load and build the factories you need in your test files, such as:

```
Factory<Product> productFactory = FactoryManager.getFactory("television");
Product product = productFactory.build();
```

6. Be happy! 

## How to build?

Use Gradle Wrapper to build this project

```
gradle wrapper   # to create gradlew file.
./gradlew build  # to generate the parser files and compile.
./gradlew jar    # to create a jar file
```

If you want to install the JAR file in your local maven repository, execute:

```
./gradlew install
```

## Disclaimer

### The project and the future

At this point of the project I don't know how far we can go with this idea!! The point is that I really like to make something similar of Factory Girl but due to the dynamic nature of Ruby, it's hard to say if we can have all major features there available. Anyway, I believe that Easy Factory would help to make our lives a little bit easier!!

## License

Easy factory is released under the MIT License:

  * http://opensource.org/licenses/MIT
