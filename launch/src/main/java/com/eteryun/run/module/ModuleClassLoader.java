package com.eteryun.run.module;

import com.eteryun.run.EteryunLaunch;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

public class ModuleClassLoader extends URLClassLoader {
    private static final Set<ModuleClassLoader> LOADERS = new CopyOnWriteArraySet<>();

    static {
        ClassLoader.registerAsParallelCapable();
    }

    public ModuleClassLoader(final @NonNull URL[] urls) {
        super(urls, EteryunLaunch.class.getClassLoader());
    }

    public void addLoaders() {
        ModuleClassLoader.LOADERS.add(this);
    }

    @Override
    protected Class<?> loadClass(final @NonNull String name, final boolean resolve) throws ClassNotFoundException {
        return this.loadClass0(name, resolve, true);
    }

    @Override
    public void close() throws IOException {
        ModuleClassLoader.LOADERS.remove(this);
        super.close();
    }

    private Class<?> loadClass0(final @NonNull String name, final boolean resolve, final boolean checkOther) throws ClassNotFoundException {
        try {
            return super.loadClass(name, resolve);
        } catch (ClassNotFoundException ignored) {
            // Ignored: we'll try others
        }

        if (checkOther) {
            for (final ModuleClassLoader loader : ModuleClassLoader.LOADERS) {
                if (loader != this) {
                    try {
                        return loader.loadClass0(name, resolve, false);
                    } catch (final ClassNotFoundException ignored) {
                        // We're trying others, safe to ignore
                    }
                }
            }
        }

        throw new ClassNotFoundException(name);
    }
}
