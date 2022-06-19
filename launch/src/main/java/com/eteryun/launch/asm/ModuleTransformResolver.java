package com.eteryun.launch.asm;

import com.eteryun.api.asm.ITransform;
import com.eteryun.api.module.Module;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

public class ModuleTransformResolver {
    private static final Logger logger = LogManager.getLogger("Eteryun Transformer");

    public static List<ITransform> resolveTransforms(Module module) {
        List<ITransform> transforms = new ArrayList<>();
        if (module.getTransforms() != null) {
            for (String className : module.getTransforms()) {
                try {
                    Class transformClass = Class.forName(className);
                    ITransform transform = (ITransform) transformClass.getConstructor().newInstance();
                    transforms.add(transform);
                } catch (Exception e) {
                    logger.error("Failed to load transform class: " + className, e);
                }
            }
        }
        return transforms;
    }
}
