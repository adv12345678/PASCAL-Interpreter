package com.hzp.interceptor.ast;

import java.util.List;

public class Block extends AST {
    private List<Declaration> declaration;
    private Compound compoundStatement;
    public Block(List<Declaration> declaration,Compound compoundStatement) {
        super(null);
        this.declaration = declaration;
        this.compoundStatement = compoundStatement;
    }

    public List<Declaration> getDeclaration() {
        return declaration;
    }

    public void setDeclaration(List<Declaration> declaration) {
        this.declaration = declaration;
    }

    public Compound getCompoundStatement() {
        return compoundStatement;
    }

    public void setCompoundStatement(Compound compoundStatement) {
        this.compoundStatement = compoundStatement;
    }
}
