# Easy factory

[![Build Status](https://travis-ci.org/travis-ci/travis-web.png?branch=master)](https://travis-ci.org/wrpinheiro/easy-factory)

## What is Easy factory?

Easy Factory is a great way to create mock data for tests.

The idea of this project is to have something similar of [Thoughtbot's Factory Girl](https://github.com/thoughtbot/factory_girl), a solution widely adopted for Ruby developers.

Currently it works only with a limited syntax for factory files. The following factories are examples of acceptable definitions.

```
factory empty_user, class br.com.easyfactory.model.User
end
```

```
factory simple_user, class br.com.easyfactory.model.User
  id: 1234
  nickname: "joseph"
  email: "joseph@josephs.com"
  name: "Joseph Climber"
  address: "Any address you might like :-)"
end
```

## How to build?

Use Gradle Wrapper to build this project

```
gradle wrapper    # to create gradlew file.
./gradlew build   # to generate the parser files and compile.
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
