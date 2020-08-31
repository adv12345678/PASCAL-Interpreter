package com.hzp.interceptor.core;

import java.util.Stack;

/**
 * 调用栈
 */
public class CallStack {
    private Stack<ActivationRecord> stack;

    public CallStack() {
        this.stack = new Stack<>();
    }

    public void push(ActivationRecord activationRecord){
        stack.push(activationRecord);
    }

    public ActivationRecord pop(){
        return stack.pop();
    }

    public ActivationRecord peek(){
        return stack.peek();
    }


    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        for(int i=stack.size()-1;i>=0;i--){
            stringBuilder.append(stack.get(i)+"\n");
        }
        return String.format("Call Stack \n %s \n",stringBuilder.toString());
    }
}
