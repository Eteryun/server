package com.example;

import com.eteryun.api.module.Module;

public class Main extends Module {
    @Override
    public void onLoad() {
        getLogger().info("OnLoad");
    }
}
