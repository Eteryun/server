package com.eteryun.run;

import com.eteryun.run.module.ModuleResolver;

import java.io.File;

public class EteryunLaunch {

    public static void main(final String[] args, File[] modules) {
        new EteryunLaunch().launch(args, modules);
    }

    public void launch(String[] args, File[] modules) {
        ModuleResolver.loadModules(modules);

        try {
            Class.forName("org.bukkit.craftbukkit.Main", true, EteryunLaunch.class.getClassLoader())
                    .getMethod("main", String[].class)
                    .invoke(null, (Object) args);
        } catch (final Throwable throwable) {
            throw new RuntimeException(throwable);
        }
    }
}
