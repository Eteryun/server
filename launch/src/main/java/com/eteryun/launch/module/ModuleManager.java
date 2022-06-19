package com.eteryun.launch.module;

import com.eteryun.api.module.IModuleManager;
import com.eteryun.api.module.Module;
import com.eteryun.api.module.ModuleConfig;
import com.eteryun.launch.agent.Agent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class ModuleManager implements IModuleManager {
    private final Logger logger = LogManager.getLogger("Eteryun Engine");
    private final List<Module> modules = new ArrayList<Module>();
    private final List<Module> shortedModules = new ArrayList<>();

    @Override
    public List<Module> getModules() {
        return shortedModules;
    }

    @Override
    public Module getModule(String name) {
        return shortedModules.stream().filter(module -> module.getName().equals(name)).findFirst().orElse(null);
    }

    public void instanceModules() {
        for (File file : ModuleLoader.locateModules()) {
            try {
                ModuleConfig config = ModuleLoader.loadConfig(file);
                if (config == null) {
                    logger.warn("Module config is null, module file: " + file.getAbsolutePath());
                    continue;
                }

                if (config.getName() == null | config.getVersion() == null) {
                    logger.warn("Module config name or version is null, module file: " + file.getAbsolutePath());
                    continue;
                }

                Agent.addJar(file.toPath());

                Logger moduleLogger = LogManager.getLogger(config.getName());
                Class<? extends  Module> moduleClass = (Class<? extends Module>) Class.forName(config.getMain());
                Object module = moduleClass.getSuperclass().getConstructor(IModuleManager.class, Logger.class, ModuleConfig.class, Path.class).newInstance(this, moduleLogger, config, file.toPath());
                modules.add((Module) module);
            } catch (IOException e) {
                logger.error("Error loading module from " + file.getName(), e);
            } catch (ClassNotFoundException | NoSuchMethodException | InstantiationException |
                     IllegalAccessException | InvocationTargetException e) {
                logger.error("Error loading module main from " + file.getName(), e);
            }
        }
    }

    public void loadModules() {
        logger.info("Loading modules...");
        shortedModules.addAll(ModuleDependencyResolver.resolveDependencies(modules));
        shortedModules.forEach(Module::onLoad);
        logger.info("Loaded " + shortedModules.size() + " modules");
    }
}
