package com.hzp.interceptor.core;

import com.hzp.interceptor.ast.*;
import com.hzp.interceptor.error.ErrorCode;
import com.hzp.interceptor.error.ParserError;
import com.hzp.interceptor.token.*;
import com.hzp.interceptor.visitor.NodeVisit;

import java.util.*;
import java.util.stream.Collectors;

public class Parser extends NodeVisit {
    private  Set<TokenType> thirdPrecedenceOpSet;
    /**
     * 当前处理token
     */
    private Token currentToken;


    private Lexer lexer;


    public Parser(Lexer lexer) {
        this.lexer = lexer;
        this.currentToken = lexer.getNextToken();
        thirdPrecedenceOpSet = new HashSet<>();
        thirdPrecedenceOpSet.addAll(Arrays.asList(new TokenType[]{TokenType.PLUS,TokenType.MINUS,TokenType.OR}));
    }



    public void error(ErrorCode errorCode,Token token) {
        throw new ParserError(String.format("Unexpected Token %s ",currentToken),token,errorCode);
    }

    /**
     * 对当前Token类型进行判断并获取下一个Token。
     * @param type
     */
    public void eat(TokenType type) {
        if(currentToken.getType().equals(type)){
            currentToken = lexer.getNextToken();
            return;
        }
        error(ErrorCode.UNEXPECTED_TOKEN,currentToken);
    }

    /**
     *
     * @return
     */
    private AST term() {
        Set<TokenType> optSet = new HashSet<>();
        optSet.addAll(Arrays.asList(new TokenType[]{TokenType.INTEGER_DIV,TokenType.MUL,TokenType.FLOAT_DIV}));

        AST node = factor();
        while (optSet.contains(currentToken.getType())){
            Token token = currentToken;
            if(token.getType().equals(TokenType.MUL)){
                eat(TokenType.MUL);
            }
            else if(token.getType().equals(TokenType.INTEGER_DIV)){
                eat(TokenType.INTEGER_DIV);
            }else if(token.getType().equals(TokenType.FLOAT_DIV)){
                eat(TokenType.FLOAT_DIV);
            }
            node = new BinOP(node,token,factor());
        }
        return node;
    }


    private Block block() {
        List<Declaration> declarationNodes =  declarations();
        Compound compoundNode = (Compound) compoundStatement();
        Block node = new Block(declarationNodes,compoundNode);
        return node;
    }
    private List<Declaration> declarations() {
        List<Declaration> declarations = new ArrayList<>();
        if(currentToken.getType().equals(TokenType.VAR)){
            eat(TokenType.VAR);
            while (currentToken.getType().equals(TokenType.ID)){
                declarations.addAll(variableDeclarations());
                eat(TokenType.SEMI);
            }
        }
        while (currentToken.getType().equals(TokenType.PROCEDURE)){
            declarations.add(procedureDeclarations());
        }
        return declarations;
    }

    public ProcedureDecl procedureDeclarations(){
        eat(TokenType.PROCEDURE);
        String procName = (String) currentToken.getValue();
        eat(TokenType.ID);

        List<Param> params = new ArrayList<>();
        if(currentToken.getType().equals(TokenType.LPAREN)){
            eat(TokenType.LPAREN);
            params = formualParameterList();
            eat(TokenType.RPAREN);
        }

        eat(TokenType.SEMI);
        Block block = block();
        ProcedureDecl procedureDecl = new ProcedureDecl(procName,params,block);
        eat(TokenType.SEMI);
        return procedureDecl;
    }

    private List<Param> formualParameterList() {
        List<Param> result = new ArrayList<>();
        result.addAll(paramters());
        while (currentToken.getType().equals(TokenType.SEMI)){
            eat(TokenType.SEMI);
            result.addAll(paramters());
        }
        return result;
    }


    private List<Param> paramters() {
        List<Token> tokens = new ArrayList<>();
        tokens.add( currentToken);
        eat(TokenType.ID);
        while (currentToken.getType().equals(TokenType.COMMA)){
            eat(TokenType.COMMA);
            tokens.add(currentToken);
            eat(TokenType.ID);
        }
        eat(TokenType.COLON);
        Type type = typeSpec();

        return tokens.stream().map(token -> new Param(new Var(token),type)).collect(Collectors.toList());
    }


    private List<VarDecl> variableDeclarations() {
        List<Var> varNodes =  new ArrayList<>();
        varNodes.add(new Var(currentToken));
        eat(TokenType.ID);
        while (currentToken.getType().equals(TokenType.COMMA)){
            eat(TokenType.COMMA);
            varNodes.add(new Var(currentToken));
            eat(TokenType.ID);
        }

        eat(TokenType.COLON);

        Type typeNode = typeSpec();

        return varNodes.stream().map(var -> new  VarDecl(var,typeNode)).collect(Collectors.toList());
    }

    private Type typeSpec() {
        Token token = currentToken;
        if(token.getType().equals(TokenType.INTEGER)){
            eat(TokenType.INTEGER);
        }else if(token.getType().equals(TokenType.REAL)){
            eat(TokenType.REAL);
        }else {
            eat(TokenType.BOOLEAN);
        }
        return new Type(token);
    }


    private AST compoundStatement() {
        eat(TokenType.BEGIN);
        List<AST> nodes = statementList();
        eat(TokenType.END);

        Compound root = new Compound();
        root.getChildren().addAll(nodes);
        return root;
    }

    private List<AST> statementList() {
        AST node = statement();
        List<AST> statementList = new ArrayList();
        statementList.add(node);
        while (currentToken.getType().equals(TokenType.SEMI)){
            eat(TokenType.SEMI);
            statementList.add(statement());
        }
        return statementList;
    }


    private AST statement() {
        AST node = null;
        if(currentToken.getType().equals(TokenType.BEGIN)){
            node = compoundStatement();
        }
        else if(currentToken.getType().equals(TokenType.ID) && lexer.getCurrentChar() == '('){
            node = procedureCalll();
        }
        else if(currentToken.getType().equals(TokenType.ID)){
            node = assignStatement();
        }else {
            node = empty();
        }
        return node;
    }

    /**
     * 分配节点
     * @return
     */
    private AST assignStatement() {
        Var left =  variable();
        Token token = currentToken;
        eat(TokenType.ASSIGN);
        AST right = thirdPrecedenceExpr();
        AST node = new Assign(left,token,right);
        return node;
    }

    /**
     * 变量节点
     * @return
     */
    private Var variable() {
        Var node = new Var(currentToken);
        eat(TokenType.ID);
        return node;
    }
    
    
    private ProcedureCall procedureCalll(){
        Token token = currentToken;
        String procName = (String) currentToken.getValue();
        List<AST> actualParams = new ArrayList<>();
        eat(TokenType.ID);
        eat(TokenType.LPAREN);
        if(currentToken.getType() != TokenType.RPAREN){
            actualParams.add(thirdPrecedenceExpr());
        }
        while (currentToken.getType() == TokenType.COMMA){
            eat(TokenType.COMMA);
            actualParams.add(thirdPrecedenceExpr());
        }
        eat(TokenType.RPAREN);
        return new ProcedureCall(currentToken,procName,actualParams);
    }

    /**
     * 空节点
     * @return
     */
    private AST empty(){
        return new NoOp();
    }

    /**
     * 返回一个因数或者一个由左右括号包围的因数节点
     * @return
     */
    private AST factor() {
        Token token = currentToken;
        if(token.getType().equals(TokenType.MINUS)){
            eat(TokenType.MINUS);
            return new UnaryOp(token, factor());
        }
        else if(token.getType().equals(TokenType.PLUS)){
            eat(TokenType.PLUS);
            return new UnaryOp(token, factor());
        }
        else if(token.getType().equals(TokenType.INTEGER_CONST)){
            eat(TokenType.INTEGER_CONST);
            return new Num(token);
        }
        else if(token.getType().equals(TokenType.REAL_CONST)){
            eat(TokenType.REAL_CONST);
            return new Num(token);
        }
        else if(token.getType().equals(TokenType.BOOLEAN_CONST)){
            eat(TokenType.BOOLEAN_CONST);
            return new BooleanValue(token);
        }
        else if(token.getType().equals(TokenType.LPAREN)){
            eat(TokenType.LPAREN);
            AST result = thirdPrecedenceExpr();
            eat(TokenType.RPAREN);
            return result;
        }else {
            AST node = variable();
            return node;
        }

    }

    private boolean isThirdPrecedenceOp(Token token){
        return thirdPrecedenceOpSet.contains(token.getType());
    }


    //第一优先级运算符 not  未实现 ~
    private AST firstPrecedenceExpr(){
        return null;
    }

    //第二优先级运算符 *, /, div , and 未实现 mod, &
    private AST secondPrecedenceExpr(){
        return null;
    }


    //第四优先级运算符 =, <>, <, <=, >, >= 未实现 in
    private AST forthPrecedenceExpr(){
        return null;
    }

    public Program program() {
        eat(TokenType.PROGRAM);

        Var var =  variable();
        String progName = var.getValue();
        eat(TokenType.SEMI);

        Block blockNode = block();
        eat(TokenType.DOT);

        return  new Program(progName,blockNode);
    }

    /**
     * 第三优先级运算符  +, -, or 未实现 |, !
     * @return
     */
    public AST thirdPrecedenceExpr() {

        AST node = term();
        while (isThirdPrecedenceOp(currentToken)){
            Token token = currentToken;
            if(token.getType().equals(TokenType.PLUS)){
                eat(TokenType.PLUS);
            }
            else if(token.getType().equals(TokenType.MINUS)){
                eat(TokenType.MINUS);
            }
            node = new BinOP(node,token,term());
        }
        return node;
    }

    public AST parse() {
        AST node = program();
        if(!currentToken.getType().equals(TokenType.EOF)){
            error(ErrorCode.UNEXPECTED_TOKEN,currentToken);
        }
        return node;
    }
}
