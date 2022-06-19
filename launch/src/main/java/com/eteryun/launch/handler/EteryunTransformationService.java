package com.eteryun.launch.handler;

import com.eteryun.launch.EteryunBootstrap;
import com.eteryun.launch.asm.EteryunTransformer;
import com.eteryun.launch.module.ModuleManager;
import cpw.mods.modlauncher.api.IEnvironment;
import cpw.mods.modlauncher.api.ITransformationService;
import cpw.mods.modlauncher.api.ITransformer;
import cpw.mods.modlauncher.api.IncompatibleEnvironmentException;
import cpw.mods.modlauncher.serviceapi.ILaunchPluginService;
import org.spongepowered.asm.launch.MixinBootstrap;
import org.spongepowered.asm.mixin.Mixins;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class EteryunTransformationService implements ITransformationService {
    private ModuleManager moduleManager = EteryunBootstrap.getInstance().getModuleManager();

    @Override
    public String name() {
        return "eteryuntransformer";
    }

    @Override
    public void initialize(IEnvironment environment) {
        MixinBootstrap.init();
        moduleManager.instanceModules();
    }

    @Override
    public void beginScanning(IEnvironment environment) {
        final ILaunchPluginService mixinService = environment.findLaunchPlugin("mixin").orElse(null);
        if (mixinService != null) {
            moduleManager.loadModules();
            moduleManager.getModules().forEach(module -> {
                if (module.getMixins() != null){
                    mixinService.offerResource(module.getPath(), module.getPath().getFileName().toString());
                    module.getMixins().forEach(Mixins::addConfiguration);
                }
            });
        }
    }

    @Override
    public void onLoad(IEnvironment env, Set<String> otherServices) throws IncompatibleEnvironmentException {

    }

    @Override
    public List<ITransformer> transformers() {
        List<ITransformer> list = new ArrayList<>();
        list.add(new EteryunTransformer());
        return list;
    }
}