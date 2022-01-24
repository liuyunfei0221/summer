package com.blue.mail.aop;


import org.springframework.asm.ClassVisitor;
import org.springframework.asm.MethodVisitor;

import static java.lang.reflect.Member.PUBLIC;


public class ReaderAdapter extends ClassVisitor {

    public ReaderAdapter(int api) {
        super(api);
    }

    @Override
    public MethodVisitor visitMethod(int access, String name, String descriptor, String signature, String[] exceptions) {



        if (access == PUBLIC) {

        }

        return super.visitMethod(access, name, descriptor, signature, exceptions);
    }

}
