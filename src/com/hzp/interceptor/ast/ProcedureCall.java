package com.hzp.interceptor.ast;

import com.hzp.interceptor.symbol.ProcedureSymbol;
import com.hzp.interceptor.token.Token;

import java.util.List;

public class ProcedureCall extends AST{
    private String procName;
    private List<AST> actualParams;
    private ProcedureSymbol procedureSymbol;
    public ProcedureCall(Token token, String procName, List<AST> actualParams) {
        super(token);
        this.procName = procName;
        this.actualParams = actualParams;
    }

    public ProcedureSymbol getProcedureSymbol() {
        return procedureSymbol;
    }

    public void setProcedureSymbol(ProcedureSymbol procedureSymbol) {
        this.procedureSymbol = procedureSymbol;
    }

    public String getProcName() {
        return procName;
    }

    public void setProcName(String procName) {
        this.procName = procName;
    }

    public List<AST> getActualParams() {
        return actualParams;
    }

    public void setActualParams(List<AST> actualParams) {
        this.actualParams = actualParams;
    }
}
