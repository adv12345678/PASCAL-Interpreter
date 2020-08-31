package com.hzp.interceptor.ast;

public class VarDecl extends Declaration {
    private Var varNode;
    private Type typeNode;

    public VarDecl(Var varNode,Type typeNode) {
        super(null);
        this.varNode = varNode;
        this.typeNode = typeNode;
    }

    public Var getVarNode() {
        return varNode;
    }

    public void setVarNode(Var varNode) {
        this.varNode = varNode;
    }

    public Type getTypeNode() {
        return typeNode;
    }

    public void setTypeNode(Type typeNode) {
        this.typeNode = typeNode;
    }
}
