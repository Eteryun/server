package com.eteryun.launch.paper;

import com.eteryun.launch.agent.Agent;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Path;

public class PaperclipService {
    private static final String PAPERCLIP_MAIN = "io.papermc.paperclip.Paperclip";

    public static void patchServer() {
        Agent.addTransformer(new PaperclipTransformer(PAPERCLIP_MAIN.replace('.', '/')));

        System.setProperty("paperclip.patchonly", "true");

        try {
            Agent.addJar(Path.of("server.jar"));
        } catch (final IOException exception) {
            throw new IllegalStateException("Unable to add paperclip jar to classpath!");
        }

        try {
            final Class<?> paperclipClass = Class.forName(PAPERCLIP_MAIN);
            paperclipClass
                    .getMethod("main", String[].class)
                    .invoke(null, (Object) new String[0]);
        } catch (final ClassNotFoundException | NoSuchMethodException | IllegalAccessException |
                       InvocationTargetException exception) {
            throw new RuntimeException(exception);
        }

        System.getProperties().remove("paperclip.patchonly");
    }
}
