package com.eteryun.api.module;

import java.util.List;

public interface IModuleManager {
    List<Module> getModules();

    Module getModule(String name);
}
