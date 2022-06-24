package com.eteryun.launch;

import com.eteryun.launch.agent.Agent;
import com.eteryun.launch.module.ModuleManager;
import com.eteryun.launch.paper.PaperclipService;
import cpw.mods.modlauncher.Launcher;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

public class EteryunBootstrap {
    private final Logger logger = LogManager.getLogger("Eteryun Bootstrap");
    private final ModuleManager moduleManager;
    private static EteryunBootstrap instance;

    public EteryunBootstrap() {
        this.moduleManager = new ModuleManager();
        EteryunBootstrap.instance = this;
    }

    public static void main(final @NonNull String[] args) {
        new EteryunBootstrap().run(args);
    }

    public void run(final @NonNull String[] args) {
        final List<String> arguments = Arrays.asList(args);
        final List<String> launchArguments = new ArrayList<>(arguments);

        launchArguments.add("--launchTarget");
        launchArguments.add("eteryunlaunch");

        PaperclipService.patchServer();

        loadDirectoryToClasspath(Path.of("./versions"));
        loadDirectoryToClasspath(Path.of("./libraries"));

        Agent.updateSecurity();

        logger.info("Eteryun Launcher v" + EteryunBootstrap.class.getPackage().getImplementationVersion());
        Launcher.main(launchArguments.toArray(new String[0]));
    }

    private void loadDirectoryToClasspath(Path dir){
        try (final Stream<Path> stream = Files.walk(dir)) {
            stream.forEach(path -> {
                if (!path.toString().endsWith(".jar")) return;

                try {
                    Agent.addJar(path);
                } catch (final IOException exception) {
                    throw new IllegalStateException("Unable to add jar at '" + path + "' to classpath!", exception);
                }
            });
        } catch (final IOException exception) {
            throw new IllegalStateException("Unable to list jars at '" + dir + "'!", exception);
        }
    }

    public static EteryunBootstrap getInstance() {
        return instance;
    }

    public ModuleManager getModuleManager() {
        return moduleManager;
    }
}
