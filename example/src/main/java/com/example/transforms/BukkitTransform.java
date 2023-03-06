package com.example.transforms;

import com.eteryun.api.asm.ITransform;
import org.objectweb.asm.*;
import org.objectweb.asm.tree.ClassNode;

public class BukkitTransform implements ITransform {
    @Override
    public String getTarget() {
        return "org/bukkit/Bukkit";
    }

    @Override
    public byte[] transform(byte[] classfileBuffer) {
        final ClassReader reader = new ClassReader(classfileBuffer);
        final ClassWriter writer = new ClassWriter(reader, ClassWriter.COMPUTE_FRAMES | ClassWriter.COMPUTE_MAXS);
        reader.accept(new BukkitClassVisitor(writer), ClassReader.EXPAND_FRAMES);
        return writer.toByteArray();
    }

    public static String getName() {
        return "Eteryun";
    }

    public static final class BukkitClassVisitor extends ClassVisitor {
        public BukkitClassVisitor(final ClassVisitor visitor) {
            super(Opcodes.ASM9, visitor);
        }

        @Override
        public MethodVisitor visitMethod(final int access, final String name, final String descriptor, final String signature, final String[] exceptions) {
            final MethodVisitor mv = this.cv.visitMethod(access, name, descriptor, signature, exceptions);
            return name.equals("getName") ? new BukkitMethodVisitor(mv) : mv;
        }

        public static final class BukkitMethodVisitor extends MethodVisitor {

            public BukkitMethodVisitor(MethodVisitor visitor) {
                super(Opcodes.ASM9, visitor);
            }

            @Override
            public void visitCode() {
                mv.visitMethodInsn(Opcodes.INVOKESTATIC, "com/example/transforms/BukkitTransform", "getName", "()Ljava/lang/String;", false);
                mv.visitInsn(Opcodes.ARETURN);
            }
        }
    }
}
