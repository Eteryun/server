package com.eteryun.launch.module;

import com.eteryun.api.module.ModuleConfig;
import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class ModuleLoader {
    private static final Gson gson = new Gson();

    public static List<File> locateModules() {
        List<File> files = new ArrayList<>();
        Path modulesPath = Path.of("modules");
        if (!modulesPath.toFile().exists()) {
            modulesPath.toFile().mkdirs();
        }
        for (File file : modulesPath.toFile().listFiles()) {
            if (file.toString().endsWith(".jar")) {
                files.add(file);
            }
        }
        return files;
    }

    public static ModuleConfig loadConfig(File file) throws IOException {
        JarFile jarFile = new JarFile(file);
        final JarEntry jarEntry = jarFile.getJarEntry("eteryun.module.json");
        if (jarEntry == null) {
            return null;
        }
        JsonReader jsonReader = new JsonReader(new InputStreamReader(jarFile.getInputStream(jarEntry), StandardCharsets.UTF_8));
        return gson.fromJson(jsonReader, ModuleConfig.class);
    }
}
