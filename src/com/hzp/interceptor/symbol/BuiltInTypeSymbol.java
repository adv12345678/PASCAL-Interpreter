package com.hzp.interceptor.symbol;

import com.hzp.interceptor.core.TokenType;

public class BuiltInTypeSymbol extends Symbol{

    public BuiltInTypeSymbol(TokenType tokenType) {
        super(tokenType.name());
    }

    public BuiltInTypeSymbol(String type) {
        super(type);
    }

    @Override
    public String toString() {
        return String.format("<{%s}(name={%s}>",this.getClass().getSimpleName(),name);
    }
}
