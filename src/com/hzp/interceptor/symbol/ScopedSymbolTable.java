package com.hzp.interceptor.symbol;

import com.hzp.interceptor.ast.Param;
import com.hzp.interceptor.core.TokenType;
import com.hzp.interceptor.token.Token;
import org.apache.log4j.Logger;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ScopedSymbolTable {

    //全局作用域
    public final static String GLOBAL_SCOPE = "globalScope";


    //基础作用域等级1
    public final static int BASE_SCOPE_LEVEL = 1;

    private static Logger logger = Logger.getLogger(ScopedSymbolTable.class);
    private Map<String,Symbol> symbolTable;

    private int scopeLevel;
    private String scopeName;
    private ScopedSymbolTable enclosingScope;

    public ScopedSymbolTable(int scopeLevel, String scopeName,ScopedSymbolTable enclosingScope) {
        this.scopeLevel = scopeLevel;
        this.scopeName = scopeName;
        this.enclosingScope = enclosingScope;
        symbolTable = new HashMap<>();
        init();
    }

    public ScopedSymbolTable getEnclosingScope() {
        return enclosingScope;
    }

    public int getScopeLevel() {
        return scopeLevel;
    }

    public void setScopeLevel(int scopeLevel) {
        this.scopeLevel = scopeLevel;
    }

    public String getScopeName() {
        return scopeName;
    }

    public void setScopeName(String scopeName) {
        this.scopeName = scopeName;
    }




    public Map<String, Symbol> getSymbolTable() {
        return symbolTable;
    }

    private void init() {
        define(new BuiltInTypeSymbol(TokenType.INTEGER.getValue()));
        define(new BuiltInTypeSymbol(TokenType.REAL.getValue()));
//        symbolTable.put(Token.INTEGER,);
//        symbolTable.put(Token.REAL,);
    }

    public void define(Symbol symbol){
        logger.debug(String.format("Define: %s",symbol));
        symbol.scopeLevel = scopeLevel;
        symbolTable.put(symbol.name,symbol);
    }

    public Symbol lookUp(String name){
        return lookUp(name,false);
    }



    public Symbol lookUp(String name,boolean currentScopeOnly){
        logger.debug(String.format("Look Up: %s.(Scope Name %s)",name,scopeName));

        Symbol symbol = symbolTable.getOrDefault(name,null);

        if(symbol != null){
            return symbol;
        }

        if(!currentScopeOnly && enclosingScope != null){
            return enclosingScope.lookUp(name);
        }

        return null;
    }

    @Override
    public String toString() {

        StringBuilder h1 = new StringBuilder("SCOPE (SCOPED SYMBOL TABLE)\n");


        h1.append(String.format("SCOPE NAME:%s   \n",scopeName));
        h1.append(String.format("SCOPE LEVEL:%d  \n",scopeLevel));
        h1.append("Scope (Scoped symbol table) contents \n");

        for(Map.Entry<String,Symbol> entry:symbolTable.entrySet()){
            h1.append(String.format("%s:%s\n",entry.getKey(),entry.getValue()));
        }

        h1.append("\n");
        return h1.toString();

    }

    public void defineByParamList(List<Param> params) {
        for(Param param:params){
            BuiltInTypeSymbol type = (BuiltInTypeSymbol) symbolTable.get(param.getType().getValue());
            VarSymbol varSymbol = new VarSymbol(param.getVar().getValue(),type);
            define(varSymbol);
        }
    }
}