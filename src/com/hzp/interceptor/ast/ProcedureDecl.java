package com.hzp.interceptor.ast;

import com.hzp.interceptor.token.Token;

import java.util.List;

public class ProcedureDecl extends Declaration {
    private String procName;
    private Block block;
    private List<Param> params;

    public ProcedureDecl(String procName,List<Param> params, Block block) {
        super(null);
        this.procName = procName;
        this.block = block;
        this.params = params;
    }

    public String getProcName() {
        return procName;
    }

    public void setProcName(String procName) {
        this.procName = procName;
    }

    public Block getBlock() {
        return block;
    }

    public void setBlock(Block block) {
        this.block = block;
    }

    public List<Param> getParams() {
        return params;
    }

    public void setParams(List<Param> params) {
        this.params = params;
    }
}
