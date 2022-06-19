package com.example.transforms;

import com.eteryun.api.asm.ITransform;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.ClassNode;

public class BukkitTransform implements ITransform {
    @Override
    public String getTarget() {
        return "org/bukkit/Bukkit";
    }

    @Override
    public ClassNode transform(ClassNode classNode) {
        //change return value to method getName
        classNode.methods.stream().filter(methodNode -> methodNode.name.equals("getName")).forEach(methodNode -> {
            methodNode.instructions.clear();
            methodNode.instructions.add(new org.objectweb.asm.tree.MethodInsnNode(Opcodes.INVOKESTATIC, "com/example/transforms/BukkitTransform", "getName", "()Ljava/lang/String;", false));
            methodNode.instructions.add(new org.objectweb.asm.tree.InsnNode(Opcodes.ARETURN));
        });

        return classNode;
    }

    public static String getName() {
        return "Eteryun";
    }
}
