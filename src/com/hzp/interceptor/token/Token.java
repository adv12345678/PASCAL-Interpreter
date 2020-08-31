package com.hzp.interceptor.token;

import com.hzp.interceptor.core.TokenType;

import java.util.Objects;

/**
 * Token类
 */
public class Token {

//    public final static  String
//            INTEGER = "INTEGER",
//            PLUS = "PLUS",
//            EOF = "EOF",
//            MINUS="MINUS",
//            MUL="MUL",
//            LPAREN="LPAREN",
//            RPAREN="RPAREN",
//            DIV="DIV",
//            DOT="DOT",
//            BEGIN="BEGIN",
//            END="END",
//            ASSIGN="ASSIGN",
//            SEMI="SEMI",
//            INTEGER_DIV="INTEGER_DIV",
//            PROGRAM="PROGRAM",
//            REAL="REAL",
//            VAR="VAR",
//            COLON="COLON",
//            COMMA="COMMA",
//            FLOAT_DIV="FLOAT_DIV",
//            REAL_CONST="REAL_CONST",
//            INTEGER_CONST="INTEGER_CONST",
//            PROCEDURE="PROCEDURE",
//            ID="ID";

    public final static Token PLUS_TOKEN = new Token(TokenType.PLUS,"+");

    public final static Token MINUS_TOKEN = new Token(TokenType.MINUS,"-");
    /**
     * 类型
     */
    private TokenType type;
    /**
     * 值
     */
    private Object value;


    private Integer lineNo;

    private Integer column;

    @Override
    public String toString() {
        return String.format("Token->(%s,'%s',position=%d:%d)",type,value,lineNo,column);
    }

    public Token(TokenType type) {
        this.type = type;
    }

    public Token(TokenType type, Object value,int lineNo,int column) {
        this.type = type;
        this.value = value;
        this.lineNo = lineNo;
        this.column = column;
    }


    public Token(TokenType type, Object value) {
        this.type = type;
        this.value = value;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == this){
            return true;
        }
        if(!(obj instanceof  Token)){
            return false;
        }
        Token token = (Token) obj;
        return token.value.equals(this.value)  && token.type.equals(this.type);
    }


    public TokenType getType() {
        return type;
    }

    public void setType(TokenType type) {
        this.type = type;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, value);
    }
}
