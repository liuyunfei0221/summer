package com.blue.mail.aop;

import org.springframework.asm.MethodVisitor;

public class ReaderMethodVisitor extends MethodVisitor {

    public ReaderMethodVisitor(int api) {
        super(api);
    }

    @Override
    public void visitCode() {


        super.visitCode();
    }
}
