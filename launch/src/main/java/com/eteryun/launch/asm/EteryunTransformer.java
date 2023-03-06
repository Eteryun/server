package com.eteryun.launch.asm;

import com.eteryun.api.asm.ITransform;
import com.eteryun.api.module.ModuleConfig;
import com.eteryun.launch.EteryunBootstrap;
import com.eteryun.launch.module.ModuleManager;

import java.lang.instrument.ClassFileTransformer;
import java.security.ProtectionDomain;
import java.util.ArrayList;
import java.util.List;

public class EteryunTransformer implements ClassFileTransformer {
    private ModuleManager moduleManager = EteryunBootstrap.getInstance().getModuleManager();
    List<ITransform> transforms = new ArrayList<>();

    public EteryunTransformer() {
        for (ModuleConfig config : moduleManager.getModulesConfig().values()) {
            if (config.getTransforms() != null) {
                transforms.addAll(ModuleTransformResolver.resolveTransforms(config.getTransforms()));
            }
        }
    }

    public byte[] transform(final ClassLoader loader, final String className,
                            final Class<?> classBeingRedefined, final ProtectionDomain protectionDomain,
                            final byte[] classfileBuffer) {
        byte[] bytes = classfileBuffer;
        for (ITransform transform : transforms) {
            if (transform.getTarget().equals(className)) {
                bytes = transform.transform(bytes);
            }
        }
        return bytes;
    }
}
