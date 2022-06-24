package com.eteryun.run.module;

import com.eteryun.api.module.Module;
import com.eteryun.api.module.ModuleConfig;
import com.eteryun.launch.module.ModuleLoader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class ModuleResolver {
    private static final Logger logger = LogManager.getLogger("Eteryun Module");

    private static Module instanceModule(File file, ModuleConfig config) {
        try {
            Logger moduleLogger = LogManager.getLogger(config.getName());

            URL url = file.toURI().toURL();
            ModuleClassLoader moduleClassLoader = new ModuleClassLoader(new URL[]{ url });
            moduleClassLoader.addLoaders();

            Class<?> aClass = moduleClassLoader.loadClass(config.getMain());

            Module module = (Module) aClass.getConstructor(Logger.class, ModuleConfig.class, Path.class).newInstance(moduleLogger, config, file.toPath());
            return module;
        } catch (IOException e) {
            logger.error("Error loading module jar from " + file.getName(), e);
        } catch (ClassNotFoundException | NoSuchMethodException | InstantiationException | IllegalAccessException |
                 InvocationTargetException e) {
            logger.error("Error loading module class from " + file.getName(), e);
        }

        return null;
    }

    public static void loadModules(File[] modulesFiles) {
        logger.info("Loading modules...");
        List<Module> modules = new ArrayList<>();
        for (File file : modulesFiles) {
            try {
                Module module = instanceModule(file, ModuleLoader.loadConfig(file));
                if (module != null) {
                    modules.add(module);
                }
            } catch (IOException e) {
                logger.error("Error loading module config from " + file.getName(), e);
            }
        }
        List<Module> loadedModules = ModuleDependencyResolver.resolveDependencies(modules);
        loadedModules.forEach(Module::onLoad);
        logger.info("Loaded " + loadedModules.size() + " modules");
    }
}
