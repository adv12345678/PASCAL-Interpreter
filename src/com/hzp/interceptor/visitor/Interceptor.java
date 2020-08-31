package com.hzp.interceptor.visitor;

import com.hzp.interceptor.ast.*;
import com.hzp.interceptor.core.*;
import com.hzp.interceptor.error.LexerError;
import com.hzp.interceptor.symbol.VarSymbol;
import org.apache.log4j.Logger;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.RecursiveAction;

public class Interceptor extends NodeVisit{
    private Parser parser;
    private static Logger logger = Logger.getLogger(Interceptor.class);

//    private Map<String,Object> globalScope = new HashMap<>();
    private ActivationRecord lastRecord;
    private CallStack callStack = new CallStack();
    public Interceptor(Parser parser) {
        this.parser = parser;
    }

    public CallStack getCallStack() {
        return callStack;
    }

    public void interpret() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, LexerError {
        AST ast = parser.parse();

        SymbolTableBuilder nodeVisit = new SymbolTableBuilder();
        nodeVisit.visit(ast);
        logger.debug(String.format("SymbolTable Content:%s",nodeVisit.getSymbolTable().getSymbolTable().values()));

        visit(ast);
    }

    public void interpret(AST ast) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, LexerError {


        SymbolTableBuilder nodeVisit = new SymbolTableBuilder();
        nodeVisit.visit(ast);
        logger.debug(String.format("SymbolTable Content:%s",nodeVisit.getSymbolTable().getSymbolTable().values()));

        visit(ast);
    }

    public Object plus(Object a, Object b){
        if(a instanceof  Double || b instanceof  Double){
            return Double.valueOf(String.valueOf(a))+Double.valueOf(String.valueOf(b));
        }
        return (int)a+(int)b;
    }
    public Object minus(Object a,Object b){
        if(a instanceof  Double || b instanceof  Double){
            return Double.valueOf(String.valueOf(a))-Double.valueOf(String.valueOf(b));
        }
        return (int)a-(int)b;
    }

    public Object mul(Object a,Object b){
        if(a instanceof  Double || b instanceof  Double){
            return Double.valueOf(String.valueOf(a))*Double.valueOf(String.valueOf(b));
        }
        return (int)a*(int)b;
    }

    public Object floatDiv(Object a,Object b){
        return Double.valueOf(String.valueOf(a))/Double.valueOf(String.valueOf(b));
    }


    public Object integerDiv(Object a,Object b){
        return Integer.valueOf(String.valueOf(a))/Integer.valueOf(String.valueOf(b));
    }

    public Object visitBooleanValue(BooleanValue booleanValue){
        return booleanValue.getValue();
    }

    protected Object visitBinOP(BinOP binOP) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        
       switch (binOP.getOp().getType()){
            case 
                    MINUS:
                return minus(visit(binOP.getLeft()),visit(binOP.getRight()));

            case 
                    PLUS:
                return plus(visit(binOP.getLeft()),visit(binOP.getRight()));

            case 
                    MUL:
                return  mul(visit(binOP.getLeft()),(int)visit(binOP.getRight()));

            case 
                    INTEGER_DIV:
                return integerDiv(visit(binOP.getLeft()),visit(binOP.getRight()));

            case 
                    FLOAT_DIV:
                return floatDiv(visit(binOP.getLeft()),visit(binOP.getRight()));
            default:
                throw new RuntimeException(String.format("unexpected op {1}",binOP.getOp().getType()));
        }
    }

    protected Object visitNum(Num num){
        return  num.getValue();
    }

    public final static int BASE_NESTING_LEVEL = 0;

    protected  void visitProgram(Program program) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        logger.debug(String.format("Enter: ProgramName %s",program.getName()));

        String procName = program.getName();
        ActivationRecord activationRecord = new ActivationRecord(ARType.PROGRAM,BASE_NESTING_LEVEL,procName);
        callStack.push(activationRecord);
        logger.debug(callStack);

        visit(program.getBlock());

        logger.debug(String.format("Leave: ProgramName %s",program.getName()));
        logger.debug(callStack);
        lastRecord = callStack.pop();
    }

    public ActivationRecord getLastRecord() {
        return lastRecord;
    }

    protected void visitBlock(Block block) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        for(Declaration varDecl: block.getDeclaration()){
            visit(varDecl);
        }
        visit(block.getCompoundStatement());
    }

    protected void visitVarDecl(VarDecl varDecl){ }
    protected void visitType(Type type){ }
    protected void visitNoOp(NoOp noOp){

    }

    protected Object visitVar(Var var){
        String varName = var.getValue();
        Object value = callStack.peek().getItem(varName);
        if(value == null){
            throw new RuntimeException(String.format("Undefined Variable %s",varName));
        }
        return value;
    }


    protected void visitAssign(Assign assign) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        String varName = assign.getLeft().getValue();
        callStack.peek().setItem(varName,visit(assign.getRight()));
    }

//    throws NoSuchMethodException, IllegalAccessException, InvocationTargetException
    protected void visitCompound(Compound compound) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        for(AST ast:compound.getChildren()){
            visit(ast);
        }
    }

    protected int visitUnaryOp(UnaryOp unaryOp) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
         TokenType type= unaryOp.getToken().getType();
        if(type.equals(TokenType.PLUS)){
            return (int) visit(unaryOp.getExpr());
        }
        else {
            return -((int)visit(unaryOp.getExpr()));
        }
    }

//    public Map<String, Object> getGlobalScope() {
//        return globalScope;
//    }

    public void visitProcedureCall(ProcedureCall procedureCall) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        String procName  = procedureCall.getProcName();
        ActivationRecord activationRecord = new ActivationRecord(ARType.PROCEDURE,procedureCall.getProcedureSymbol().getScopeLevel(),procName);
        List<VarSymbol> formalParam = procedureCall.getProcedureSymbol().getParams();
        for(int i=0;i<procedureCall.getActualParams().size();i++){
            activationRecord.setItem(formalParam.get(i).getName(),visit(procedureCall.getActualParams().get(i)));
        }
        callStack.push(activationRecord);

        logger.debug(String.format("Enter Procedure:%s",procedureCall.getProcName()));
        logger.debug(callStack);
        visit(procedureCall.getProcedureSymbol().getBlockAST());
        logger.debug(String.format("Leave Procedure:%s",procedureCall.getProcName()));
        logger.debug(callStack);

        callStack.pop();
    }

    public void visitProcedureDecl(ProcedureDecl procedureDecl){}
}
