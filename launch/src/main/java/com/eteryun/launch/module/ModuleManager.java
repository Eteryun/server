package com.eteryun.launch.module;

import com.eteryun.api.module.ModuleConfig;
import com.eteryun.launch.agent.Agent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

public class ModuleManager {
    private final Logger logger = LogManager.getLogger("Eteryun Engine");
    private final HashMap<File, ModuleConfig> modulesConfig = new HashMap();

    public HashMap<File, ModuleConfig> getModulesConfig() {
        return modulesConfig;
    }

    public void loadConfigs() {
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

                modulesConfig.put(file, config);
            } catch (IOException e) {
                logger.warn("Module config load error, module file: " + file.getAbsolutePath());
            }
        }
    }
}
