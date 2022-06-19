package com.eteryun.api.module;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.nio.file.Path;
import java.util.List;

public class Module {
    private Logger logger;

    private ModuleConfig config;

    private IModuleManager moduleManager;

    private Path path;

    public Module() {
    }

    public Module(IModuleManager moduleManager, Logger logger, ModuleConfig config, Path path) {
        this.logger = logger;
        this.config = config;
        this.moduleManager = moduleManager;
        this.path = path;
    }

    public void onLoad() {
        logger.info("Loaded");
    }

    public String getName() {
        return config.getName();
    }

    public String getVersion() {
        return config.getVersion();
    }

    public List<String> getDependencies() {
        return config.getDependencies();
    }

    public List<String> getMixins() {
        return config.getMixins();
    }

    public List<String> getTransforms() {
        return config.getTransforms();
    }

    public Module getModule(String name) {
        return moduleManager.getModule(name);
    }

    public Path getPath() {
        return path;
    }

    public Logger getLogger() {
        return logger;
    }
}
