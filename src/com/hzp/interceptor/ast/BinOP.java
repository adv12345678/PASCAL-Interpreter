package com.hzp.interceptor.ast;

import com.hzp.interceptor.token.Token;

public class BinOP extends AST{
    private Token op;
    private AST left;
    private AST right;

    public BinOP(AST left,Token op,AST right) {
        super(op);
        this.op = op;
        this.left = left;
        this.right = right;
    }

    public Token getOp() {
        return op;
    }

    public void setOp(Token op) {
        this.op = op;
    }

    public AST getLeft() {
        return left;
    }

    public void setLeft(AST left) {
        this.left = left;
    }

    public AST getRight() {
        return right;
    }

    public void setRight(AST right) {
        this.right = right;
    }
}
