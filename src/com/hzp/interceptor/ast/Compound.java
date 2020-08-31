package com.hzp.interceptor.ast;

import java.util.ArrayList;
import java.util.List;

public class Compound extends AST{
    private List<AST> children;

    public Compound() {
        super(null);
        this.children =  new ArrayList<>();
    }

    public List<AST> getChildren() {
        return children;
    }

    public void setChildren(List<AST> children) {
        this.children = children;
    }
}
