package com.hzp.interceptor.symbol;

import com.hzp.interceptor.ast.Block;
import com.hzp.interceptor.ast.Param;

import java.util.List;

public class ProcedureSymbol extends Symbol{
    private List<VarSymbol> params;


    private Block blockAST;

    public ProcedureSymbol(String name, List<VarSymbol> params) {
        super(name);
        this.params = params;
    }

    public Block getBlockAST() {
        return blockAST;
    }

    public void setBlockAST(Block blockAST) {
        this.blockAST = blockAST;
    }

    @Override
    public String toString() {
        return String.format("<{%s}(name={%s}, parameters={%s})>",this.getClass().getSimpleName(),name,params);
    }

    public List<VarSymbol> getParams() {
        return params;
    }
}
