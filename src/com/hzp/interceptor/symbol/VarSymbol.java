package com.hzp.interceptor.symbol;

public class VarSymbol extends Symbol {

    public VarSymbol(String name, BuiltInTypeSymbol type) {
        super(name, type);
    }

    @Override
    public String toString() {
       return String.format("<{%s}(name={%s}, type={%s})>",this.getClass().getSimpleName(),name,type.getName());
    }


}
