package com.hzp.interceptor.core;

import com.hzp.interceptor.error.ErrorCode;
import com.hzp.interceptor.error.LexerError;
import com.hzp.interceptor.token.Token;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Lexer {
    /**
     * 文本
     */
    private String text;


    /**
     * 偏移
     */
    private int offset;


    /**
     * 当前读取字符所处行编号
     */
    private int lineNo;

    /**
     * 当前读取字符所处列数。
     */
    private int column;


    private Character currentChar;


    private static HashMap<String,Token> RESERVED_WORD = new HashMap<>();
    private static Set<String> BOOLEAN_CONST_SET = new HashSet<>();
    private static String BOOLEAN_TRUE = "true";
    private static String BOOLEAN_FALSE = "false";
    static {

        BOOLEAN_CONST_SET.add(BOOLEAN_TRUE);
        BOOLEAN_CONST_SET.add(BOOLEAN_FALSE);


        TokenType[] tokenTypes = TokenType.values();
        int i=0;
        for(;i<tokenTypes.length;i++){
            if(tokenTypes[i].getValue().equals("PROGRAM")){
                break;
            }
        }

        for(;i<tokenTypes.length;i++){
            TokenType tokenType = tokenTypes[i];
            RESERVED_WORD.put(tokenType.getValue(),new Token(tokenType,tokenType.getValue()));
            if(tokenTypes[i].getValue().equals("END")){
                break;
            }
        }
    }


    /**
     * 跳过注释
     */
    public void skipComment(){
        while (currentChar != null && currentChar != '}'){
            advance();
        }
        advance();
    }

    public Lexer(String text){
        this.text = text;
        this.offset = 0;
        this.currentChar = text.charAt(offset);
        this.lineNo = 0;
        this.column = 0;

    }

    public Character getCurrentChar() {
        return currentChar;
    }

    public void error() throws LexerError {
        String message  = String.format("LexerError on char %s,line: %d,column: %d,",currentChar,lineNo,currentChar);
        throw new LexerError(message);
    }



    /**
     * 获取文本中下一个Token
     * @return
     */
    public Token getNextToken() throws LexerError {

        while (currentChar != null ){
            //去掉多余空格
            if(Character.isWhitespace(currentChar)){
                skipWhiteSpace();
                continue;
            }

            if(currentChar == '='){
                advance();
                return new Token(TokenType.EQUAL,"=",lineNo,column);
            }

            if(currentChar == '>' && peek() == '='){
                advance();
                advance();
                return new Token(TokenType.GREATER_EQUAL,">=",lineNo,column);
            }

            if(currentChar == '<' && peek() == '>'){
                advance();
                advance();
                return new Token(TokenType.NOT_EQUAL,"<>",lineNo,column);
            }


            if(currentChar == '<' && peek() == '='){
                advance();
                advance();
                return new Token(TokenType.LESS_EQUAL,"<=",lineNo,column);
            }


            if(currentChar == '<'){
                advance();
                return new Token(TokenType.LESS,"<",lineNo,column);
            }

            if(currentChar == '>'){
                advance();
                return new Token(TokenType.GREATER,">",lineNo,column);
            }




            if(currentChar == ':' && peek() == '='){
                advance();
                advance();
                return new Token(TokenType.ASSIGN,":=",lineNo,column);
            }


            if(currentChar == '{'){
                advance();
                skipComment();
                continue;
            }

            if(Character.isLetter(currentChar)){
                return id();
            }

            //遇到数字则读取后面的数字然后将其合并为一个整数
            if(Character.isDigit(currentChar)){
                return number();
            }

            TokenType tokenType = getTokenType();
            if(tokenType != null){
                Token token = new Token(tokenType,currentChar,lineNo,column);
                advance();
                return token;
            }


            error();
        }

        return new Token(TokenType.EOF,null);
    }

    private TokenType getTokenType() {
        TokenType[] tokenTypes = TokenType.values();
        TokenType currentTokenType = null;
        for(TokenType tokenType:tokenTypes){
            if(tokenType.getValue().equals(String.valueOf(currentChar))){
                currentTokenType = tokenType;
                break;
            }
        }
        return currentTokenType;
    }


    /**
     * 从当前指针向后开始读取一个数。
     * @return
     */
    private Integer integer() {
        StringBuilder stringBuilder = new StringBuilder();
        while (currentChar != null && Character.isDigit(currentChar)){
            stringBuilder.append(currentChar);
            advance();
        }
        return Integer.parseInt(stringBuilder.toString());
    }


    /**
     * 从当前指针向后开始读取一个数。
     * @return
     */
    private Token number() {
        StringBuilder stringBuilder = new StringBuilder();
        while (currentChar != null && Character.isDigit(currentChar)){
            stringBuilder.append(currentChar);
            advance();
        }
        Token token = null;
        if(currentChar == '.'){
            stringBuilder.append(currentChar);
            advance();
            while (currentChar != null && Character.isDigit(currentChar)){
                stringBuilder.append(currentChar);
                advance();
            }
            token = new Token(TokenType.REAL_CONST,Float.parseFloat(stringBuilder.toString()),lineNo,column);
        }
        else {
            token = new Token(TokenType.INTEGER_CONST,Integer.parseInt(stringBuilder.toString()),lineNo,column);
        }
        return token;
    }





    private Token id(){
        StringBuilder stringBuilder = new StringBuilder();
        while (currentChar != null && Character.isLetterOrDigit(currentChar)){
            stringBuilder.append(currentChar);
            advance();
        }
        String result = stringBuilder.toString();

        //如果是布尔值返回BOOLEAN_CONST的Token。
        if(isBooleanValue(result)){
            return new Token(TokenType.BOOLEAN_CONST,Boolean.valueOf(result));
        }

        return RESERVED_WORD.getOrDefault(result.toUpperCase(),new  Token(TokenType.ID,result,lineNo,column));
    }

    /**
     * string的值是否为布尔函数
     * @param string
     * @return
     */
    private boolean isBooleanValue(String string) {
        return BOOLEAN_CONST_SET.contains(string);
    }

    /**
     * 跳过空格
     */
    private void skipWhiteSpace() {
        while (currentChar != null && currentChar == ' '||currentChar == '\n'){
            advance();
        }
    }

    /**
     * 将指针加1，重新获取当前char,如果超出返回一个null。
     */
    private void advance() {

        if(currentChar == '\n'){
            lineNo+=1;
            column = 0;
        }

        offset++;

        if(offset < text.length()){
            column+=1;
            currentChar = text.charAt(offset);
        }else {
            currentChar = null;
        }
    }


    public Character peek(){
        int peekPos = offset+1;
        if(peekPos >= text.length()){
            return null;
        }
        return text.charAt(peekPos);
    }


}
