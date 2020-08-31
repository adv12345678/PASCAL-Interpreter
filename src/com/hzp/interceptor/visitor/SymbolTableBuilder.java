package com.hzp.interceptor.visitor;

import com.hzp.interceptor.ast.*;
import com.hzp.interceptor.exception.UndefinedNameException;
import com.hzp.interceptor.symbol.BuiltInTypeSymbol;
import com.hzp.interceptor.symbol.Symbol;
import com.hzp.interceptor.symbol.SymbolTable;
import com.hzp.interceptor.symbol.VarSymbol;

import java.lang.reflect.InvocationTargetException;

public class SymbolTableBuilder extends NodeVisit{
    private SymbolTable symbolTable = new SymbolTable();

    public SymbolTable getSymbolTable(){
        return symbolTable;
    }


    public void visitAssign(Assign assign) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        visit(assign.getLeft());
        visit(assign.getRight());
    }
    public void visitBinOP(BinOP binOP) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        visit(binOP.getLeft());
        visit(binOP.getRight());
    }
    public void visitBlock(Block block) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        for(Declaration varDecl:block.getDeclaration()){
            visit(varDecl);
        }
        visit(block.getCompoundStatement());
    }
    public void visitCompound(Compound compound) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        for(AST ast:compound.getChildren()){
            visit(ast);
        }
    }
    public void visitProgram(Program program) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        visit(program.getBlock());
    }
    public void visitType(Type type){
        symbolTable.define(new BuiltInTypeSymbol(type.getValue()));
    }

    public void visitVar(Var var){
        VarSymbol varSymbol = (VarSymbol) symbolTable.lookUp(var.getValue());
        if(varSymbol == null){
            throw new UndefinedNameException(String.format("Unexpected Name %s",var.getValue()));
        }
    }
    public void visitVarDecl(VarDecl varDecl){
        BuiltInTypeSymbol builtInTypeSymbol = new BuiltInTypeSymbol(varDecl.getTypeNode().getValue());
        Symbol symbol = new VarSymbol(varDecl.getVarNode().getValue(),builtInTypeSymbol);
        symbolTable.define(symbol);
    }
    public void visitProcedureCall(ProcedureCall procedureCall){
    }

    public void visitBooleanValue(BooleanValue booleanValue){
    }

    public void visitUnaryOp(UnaryOp unaryOp) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        visit(unaryOp.getExpr());
    }
    public void visitNoOp(NoOp noOp){}
    public void visitNum(Num num){}
    public void visitProcedureDecl(ProcedureDecl procedureDecl){}
}
