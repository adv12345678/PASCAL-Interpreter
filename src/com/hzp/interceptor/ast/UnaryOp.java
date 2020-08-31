package com.hzp.interceptor.ast;

import com.hzp.interceptor.token.Token;

public class UnaryOp extends AST {
    private AST expr;

    public UnaryOp(Token op, AST expr) {
        super(op);
        this.expr = expr;
    }


    public AST getExpr() {
        return expr;
    }

    public void setExpr(AST expr) {
        this.expr = expr;
    }
}
