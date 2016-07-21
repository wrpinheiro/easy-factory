grammar EasyFactory;

@header {
 package com.thecodeinside.easyfactory.parser;
}

factoriesDecl
    :   (factoryDecl)*
    ;

factoryDecl
    :   'factory' Identifier ',' classDecl attributeListDecl 'end'
    ;

classDecl
    :   'class' qualifiedName
    ;

attributeListDecl
    :   (attributeDecl)*
    ;

attributeDecl
    :   Identifier ':' expression
    ;

expression
   :   literal
   |   buildFactoryAttributeDecl
   ;

literal
    :   integerLiteral
    |   stringLiteral
    |   nullLiteral
    ;

integerLiteral
    :   IntegerLiteral
    ;

stringLiteral
    :   StringLiteral
    ;

nullLiteral
    :   'null'
    ;

arrayLiteral
    :   '[' literalListDecl? ','? ']'
    ;

literalListDecl
    :  literal (',' literal)*
    ;

IntegerLiteral
    :   SignedDecimalNumeral;

SignedDecimalNumeral
    : Sign? DecimalNumeral
    ; 
    
fragment
Sign
    :   [+-]
    ;

DecimalNumeral
    :   '0'
    |   [1-9] [0-9]*
    ;

StringLiteral
    :   '"' StringCharacters? '"'
    ;

fragment
StringCharacters
    :   StringCharacter+
    ;

fragment
StringCharacter
    :   ~["\\]
    ;

buildFactoryAttributeDecl
    :   'build' '(' identifierListDecl ')'
    ;

identifierListDecl
    :  Identifier (',' Identifier)*
    ;


qualifiedName
    :   Identifier ('.' Identifier)*
    ;

Identifier
    :   JavaLetter JavaLetterOrDigit*
    ;

fragment
JavaLetter
    :   [a-zA-Z$_]
    |  ~[\u0000-\u00FF\uD800-\uDBFF]
        {Character.isJavaIdentifierStart(_input.LA(-1))}?
    |   [\uD800-\uDBFF] [\uDC00-\uDFFF]
        {Character.isJavaIdentifierStart(Character.toCodePoint((char)_input.LA(-2), (char)_input.LA(-1)))}?
    ;

fragment
JavaLetterOrDigit
    :   [a-zA-Z0-9$_]
    |   ~[\u0000-\u00FF\uD800-\uDBFF]
        {Character.isJavaIdentifierPart(_input.LA(-1))}?
    |   [\uD800-\uDBFF] [\uDC00-\uDFFF]
        {Character.isJavaIdentifierPart(Character.toCodePoint((char)_input.LA(-2), (char)_input.LA(-1)))}?
    ;

INT
    :   [0-9]+
    ;
WS
    :  [ \t\r\n\u000C]+ -> skip
    ;

