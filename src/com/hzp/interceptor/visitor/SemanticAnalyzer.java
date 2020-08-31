package com.hzp.interceptor.visitor;


import com.hzp.interceptor.ast.*;
import com.hzp.interceptor.error.ErrorCode;
import com.hzp.interceptor.error.SemanticError;
import com.hzp.interceptor.symbol.*;
import com.hzp.interceptor.token.Token;
import org.apache.log4j.Logger;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.stream.Collectors;

public class SemanticAnalyzer extends NodeVisit{
    private ScopedSymbolTable currentSymbolTable;



    private static Logger logger = Logger.getLogger(SemanticAnalyzer.class);

    public SemanticAnalyzer() {
    }

    public void visitAssign(Assign assign) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        visit(assign.getLeft());
        //类型和赋值应该相匹配
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

    public void visitBooleanValue(BooleanValue booleanValue){
    }

    public void visitProcedureCall(ProcedureCall procedureCall) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        ProcedureSymbol procedureSymbol = (ProcedureSymbol) currentSymbolTable.lookUp(procedureCall.getProcName());
        if(procedureCall == null){
            error(ErrorCode.ID_NOT_FOUND, procedureCall.getToken());

        }
        List<VarSymbol> paramList=  procedureSymbol.getParams();
        if(paramList.size() !=  procedureCall.getActualParams().size()){
            error(ErrorCode.PARAMETER_NUMBER_NO_MATCH, procedureCall.getToken());
        }
        for(AST param: procedureCall.getActualParams()){
            visit(param);
        }
        procedureCall.setProcedureSymbol(procedureSymbol);
    }

    public void visitProgram(Program program) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        logger.debug("enter global scope");
        currentSymbolTable = new ScopedSymbolTable(ScopedSymbolTable.BASE_SCOPE_LEVEL,ScopedSymbolTable.GLOBAL_SCOPE,currentSymbolTable);
        visit(program.getBlock());

        logger.debug(currentSymbolTable);
        logger.debug("leave global scope");
    }

    public void visitType(Type type){
        currentSymbolTable.define(new BuiltInTypeSymbol(type.getValue()));
    }

    public void visitVar(Var var){
        VarSymbol varSymbol = (VarSymbol) currentSymbolTable.lookUp(var.getValue());

        if(varSymbol == null){
            error(ErrorCode.ID_NOT_FOUND,var.getToken());
        }
    }
    public void visitVarDecl(VarDecl varDecl){
        BuiltInTypeSymbol builtInTypeSymbol = new BuiltInTypeSymbol(varDecl.getTypeNode().getValue());
        Symbol symbol = new VarSymbol(varDecl.getVarNode().getValue(),builtInTypeSymbol);

        if(currentSymbolTable.lookUp(symbol.getName(),true) != null){
            error(ErrorCode.DUPLICATE_ID,varDecl.getToken());
        }

        currentSymbolTable.define(symbol);
    }

    private void error(ErrorCode errorCode, Token token) {
        throw new SemanticError(String.format("{%s} -> {%s}",errorCode.getValue(),token),token,errorCode);
    }


    public void visitUnaryOp(UnaryOp unaryOp) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        visit(unaryOp.getExpr());
    }

    public void visitNoOp(NoOp noOp){}
    public void visitNum(Num num){}
    public void visitProcedureDecl(ProcedureDecl procedureDecl) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        String procName = procedureDecl.getProcName();
        ProcedureSymbol procedureSymbol = new ProcedureSymbol(
                procName,
                procedureDecl.getParams().stream().
                        map(a -> new VarSymbol(
                                a.getVar().getValue(),
                                new BuiltInTypeSymbol(a.getType().getValue()))).collect(Collectors.toList())
        );
        currentSymbolTable.define(procedureSymbol);
        logger.debug(String.format("enter scope %s",procName));
        ScopedSymbolTable scopedSymbolTable = new ScopedSymbolTable(
                currentSymbolTable.getScopeLevel()+1,
                procName,
                currentSymbolTable);
        currentSymbolTable = scopedSymbolTable;
        scopedSymbolTable.defineByParamList(procedureDecl.getParams());
        visit(procedureDecl.getBlock());

        logger.debug(currentSymbolTable);
        logger.debug(String.format("leave scope %s",procName));
        currentSymbolTable = currentSymbolTable.getEnclosingScope();
        procedureSymbol.setBlockAST(procedureDecl.getBlock());
    }
}
