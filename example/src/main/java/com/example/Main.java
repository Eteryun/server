package com.example;

import com.eteryun.api.module.IModuleManager;
import com.eteryun.api.module.Module;
import com.eteryun.api.module.ModuleConfig;
import org.apache.logging.log4j.Logger;

import java.nio.file.Path;

public class Main extends Module {
    public Main(IModuleManager moduleManager, Logger logger, ModuleConfig config, Path path) {
        super(moduleManager, logger, config, path);
    }

    @Override
    public void onLoad() {
        getLogger().info("OnLoad");
    }
}
