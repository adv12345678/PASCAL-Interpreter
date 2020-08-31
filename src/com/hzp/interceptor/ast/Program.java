package com.hzp.interceptor.ast;

public class Program extends AST {
    private String name;
    private Block block;
    public Program(String name,Block block) {
        super(null);
        this.name = name;
        this.block = block;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Block getBlock() {
        return block;
    }

    public void setBlock(Block block) {
        this.block = block;
    }
}
