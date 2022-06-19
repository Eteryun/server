package com.eteryun.launch.paper;

import org.objectweb.asm.*;

import java.lang.instrument.ClassFileTransformer;
import java.security.ProtectionDomain;

public class PaperclipTransformer implements ClassFileTransformer {
    private final String target;

    public PaperclipTransformer(final String target) {
        this.target = target;
    }

    public byte[] transform(final ClassLoader loader, final String className,
                            final Class<?> classBeingRedefined, final ProtectionDomain protectionDomain,
                            final byte[] classfileBuffer) {
        if (!className.equals(this.target)) return null;
        final ClassReader reader = new ClassReader(classfileBuffer);
        final ClassWriter writer = new ClassWriter(reader, ClassWriter.COMPUTE_FRAMES | ClassWriter.COMPUTE_MAXS);
        reader.accept(new PaperclipClassVisitor(writer), ClassReader.EXPAND_FRAMES);
        return writer.toByteArray();
    }

    public static final class PaperclipClassVisitor extends ClassVisitor {
        public PaperclipClassVisitor(final ClassVisitor visitor) {
            super(Opcodes.ASM9, visitor);
        }

        @Override
        public MethodVisitor visitMethod(final int access, final String name, final String descriptor, final String signature, final String[] exceptions) {
            final MethodVisitor mv = this.cv.visitMethod(access, name, descriptor, signature, exceptions);
            return new PaperclipMethodVisitor(descriptor, mv);
        }
    }

    public static final class PaperclipMethodVisitor extends MethodVisitor {
        private final String descriptor;

        public PaperclipMethodVisitor(final String descriptor, final MethodVisitor visitor) {
            super(Opcodes.ASM9, visitor);

            this.descriptor = descriptor;
        }

        @Override
        public void visitMethodInsn(final int opcode, final String owner, final String name, final String descriptor, final boolean isInterface) {
            if (name.equals("setupClasspath")) {
                super.visitMethodInsn(opcode, owner, name, descriptor, isInterface);
                // After the method is written return.
                this.visitInsn(Opcodes.RETURN);
                return;
            }

            // Return before system exit calls.
            if (owner.equals("java/lang/System") && name.equals("exit")) {
                if (this.descriptor.endsWith("V")) {
                    // Void descriptor return type, will return normally...
                    this.visitInsn(Opcodes.RETURN);
                } else {
                    // Otherwise, return null.
                    this.visitInsn(Opcodes.ACONST_NULL);
                    this.visitInsn(Opcodes.ARETURN);
                }
            }

            super.visitMethodInsn(opcode, owner, name, descriptor, isInterface);
        }
    }
}
