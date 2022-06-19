package com.eteryun.api.module;

import com.google.gson.annotations.SerializedName;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.List;
import java.util.Objects;

public class ModuleConfig {
    private @SerializedName("name") String name;
    private @SerializedName("version") String version;
    private @SerializedName("main") String main;
    private @SerializedName("dependencies") List<String> dependencies;
    private @SerializedName("mixins") List<String> mixins;
    private @SerializedName("transforms") List<String> transforms;

    public ModuleConfig() {
    }

    public ModuleConfig(final @NonNull String name,
                        final @NonNull String version,
                        final @Nullable String main) {
        this.name = name;
        this.version = version;
        this.main = main;
    }

    public ModuleConfig(final @NonNull String name,
                        final @NonNull String version,
                        final @Nullable String main,
                        final @Nullable List<String> dependencies,
                        final @Nullable List<String> mixins,
                        final @Nullable List<String> transforms) {
        this.name = name;
        this.version = version;
        this.main = main;
        this.dependencies = dependencies;
        this.mixins = mixins;
        this.transforms = transforms;
    }

    public String getName() {
        return name;
    }

    public String getVersion() {
        return version;
    }

    public String getMain() {
        return main;
    }

    public List<String> getDependencies() {
        return dependencies;
    }

    public List<String> getMixins() {
        return mixins;
    }

    public List<String> getTransforms() {
        return transforms;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ModuleConfig that = (ModuleConfig) o;
        return name.equals(that.name) && version.equals(that.version) && main.equals(that.main) && Objects.equals(dependencies, that.dependencies) && Objects.equals(mixins, that.mixins) && Objects.equals(transforms, that.transforms);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, version, main, dependencies, mixins, transforms);
    }

    @Override
    public String toString() {
        return "ModuleConfig{" +
                "name='" + name + '\'' +
                ", version='" + version + '\'' +
                ", main='" + main + '\'' +
                ", dependencies=" + dependencies +
                ", mixins=" + mixins +
                ", transforms=" + transforms +
                '}';
    }
}
