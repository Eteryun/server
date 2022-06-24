package com.eteryun.api.module;

import org.apache.logging.log4j.Logger;

import java.nio.file.Path;
import java.util.List;

public class Module {
    private final Logger logger;

    private final ModuleConfig config;
    private final Path path;

    public Module(Logger logger, ModuleConfig config, Path path) {
        this.logger = logger;
        this.config = config;
        this.path = path;
    }

    public void onLoad() {
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

    public Path getPath() {
        return path;
    }

    public Logger getLogger() {
        return logger;
    }
}
