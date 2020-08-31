package com.hzp.interceptor.core;

import com.sun.xml.internal.bind.v2.model.core.ID;

public enum TokenType {
    ASSIGN(":="),
    COLON(":"),
    COMMA(","),
    DOT("."),
    FLOAT_DIV("/"),

    PROGRAM("PROGRAM"),
    BEGIN("BEGIN"),
    INTEGER("INTEGER"),
    REAL("REAL"),
    BOOLEAN("BOOLEAN"),
    INTEGER_DIV("DIV"),
    VAR("VAR"),
    NOT("NOT"),
    AND("AND"),
    OR("OR"),
    PROCEDURE("PROCEDURE"),
    END("END"),


    EQUAL("EQUAL"),
    NOT_EQUAL("NOT_EQUAL"),
    LESS("<"),
    LESS_EQUAL("<="),

    GREATER(">"),
    GREATER_EQUAL(">="),


    EOF(""),
    ID("ID"),
    INTEGER_CONST("INTEGER_CONST"),
    BOOLEAN_CONST("BOOLEAN_CONST"),
    LPAREN("("),
    MINUS("-"),
    MUL("*"),
    PLUS("+"),
    REAL_CONST("REAL_CONST"),
    RPAREN(")"),
    SEMI(";");


    private String value;

    TokenType(String value) {
        this.value = value;
    }


    public String getValue() {
        return value;
    }
}
