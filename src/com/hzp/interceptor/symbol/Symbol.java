package com.hzp.interceptor.symbol;

public class Symbol {
    protected String name;
    protected BuiltInTypeSymbol type;
    protected int scopeLevel;
    public Symbol(String name) {
        this.name = name;
    }

    public Symbol(String name, BuiltInTypeSymbol type) {
        this.name = name;
        this.type = type;
    }

    public int getScopeLevel() {
        return scopeLevel;
    }

    public void setScopeLevel(int scopeLevel) {
        this.scopeLevel = scopeLevel;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
