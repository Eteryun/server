package com.eteryun.api.asm;

public interface ITransform {
    public String getTarget();
    public byte[] transform(byte[] classfileBuffer);
}
