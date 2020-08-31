package com.hzp.interceptor.symbol;

import com.hzp.interceptor.core.TokenType;
import org.apache.log4j.Logger;

import java.util.HashMap;
import java.util.Map;

public class SymbolTable {

    private static Logger logger = Logger.getLogger(SymbolTable.class);
    private Map<String,Symbol> symbolTable;

    public SymbolTable() {
        symbolTable = new HashMap<>();
        init();
    }


    public Map<String, Symbol> getSymbolTable() {
        return symbolTable;
    }

    private void init() {
        symbolTable.put(TokenType.INTEGER.name(),new BuiltInTypeSymbol(TokenType.INTEGER));
        symbolTable.put(TokenType.REAL.name(),new BuiltInTypeSymbol(TokenType.REAL));
    }

    public void define(Symbol symbol){
        logger.debug(String.format("Define: %s",symbol));
        symbolTable.put(symbol.name,symbol);
    }

    public Symbol lookUp(String name){
        logger.debug(String.format("Look Up: %s",name));
        return symbolTable.getOrDefault(name,null);
    }
}
