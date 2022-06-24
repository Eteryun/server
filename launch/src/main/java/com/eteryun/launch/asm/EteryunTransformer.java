package com.eteryun.launch.asm;

import com.eteryun.api.asm.ITransform;
import com.eteryun.api.module.Module;
import com.eteryun.api.module.ModuleConfig;
import com.eteryun.launch.EteryunBootstrap;
import com.eteryun.launch.module.ModuleManager;
import cpw.mods.modlauncher.api.ITransformer;
import cpw.mods.modlauncher.api.ITransformerVotingContext;
import cpw.mods.modlauncher.api.TransformerVoteResult;
import org.objectweb.asm.tree.ClassNode;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class EteryunTransformer implements ITransformer<ClassNode> {
    private ModuleManager moduleManager = EteryunBootstrap.getInstance().getModuleManager();
    List<ITransform> transforms = new ArrayList<>();

    public EteryunTransformer() {
        for (ModuleConfig config : moduleManager.getModulesConfig().values()) {
            if (config.getTransforms() != null) {
                transforms.addAll(ModuleTransformResolver.resolveTransforms(config.getTransforms()));
            }
        }
    }

    @Override
    public ClassNode transform(ClassNode classNode, ITransformerVotingContext context) {
        for (ITransform transform : transforms) {
            if (transform.getTarget().equals(classNode.name)) {
                transform.transform(classNode);
            }
        }
        return classNode;
    }

    @Override
    public TransformerVoteResult castVote(ITransformerVotingContext context) {
        return TransformerVoteResult.YES;
    }

    @Override
    public Set<Target> targets() {
        HashSet<Target> hashset = new HashSet<>();

        transforms.forEach(transform -> {
            hashset.add(Target.targetClass(transform.getTarget()));
        });

        return hashset;
    }
}
