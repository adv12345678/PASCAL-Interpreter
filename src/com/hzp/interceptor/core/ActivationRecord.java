package com.hzp.interceptor.core;

import java.util.HashMap;
import java.util.Map;

public class ActivationRecord {
    /**
     * 激活记录类型
     */
    private ARType arType;
    /**
     * 嵌套等级
     */
    private int nestingLevel;
    private Map<String,Object> members;
    /**
     * 名字
     */
    private String name;

    public ActivationRecord(ARType arType, int nestingLevel, String name) {
        this.arType = arType;
        this.nestingLevel = nestingLevel;
        this.name = name;
        members = new HashMap<>();
    }

    public Object getItem(String key){
        return members.get(key);
    }

    public void setItem(String key,Object object){
        members.put(key,object);
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder("");
        stringBuilder.append(String.format("{%d}: {%s} {%s}\n",nestingLevel,name,arType));
        for(Map.Entry<String,Object> entry:members.entrySet()){
            stringBuilder.append(String.format("{%s}:{%s} \n",entry.getKey(),entry.getValue()));
        }
        return stringBuilder.toString();
    }
}
