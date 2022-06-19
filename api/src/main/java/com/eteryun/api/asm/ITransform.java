package com.eteryun.api.asm;

import org.objectweb.asm.tree.ClassNode;

public interface ITransform {
    public String getTarget();
    public ClassNode transform(ClassNode classNode);
}
