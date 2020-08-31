package com.hzp.interceptor.visitor;

import com.hzp.interceptor.ast.AST;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class NodeVisit {
    /**
     * 访问AST节点，并根据节点类型，使用反射调用相应的方法处理节点。
     * @param ast
     * @return
     * @throws NoSuchMethodException
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     */
    public Object visit(AST ast) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        String methodName  = "visit"+ast.getClass().getSimpleName();
        Method method = this.getClass().getDeclaredMethod(methodName,ast.getClass());
        method.setAccessible(true);
        if(method.getReturnType().getName().equals("void")){
            method.invoke(this,ast);
            return -1;
        }

        Object val =  method.invoke(this,ast);
        return val;
    }

}
