package com.nikonru.spawntimeout;

import com.nikonru.spawntimeout.config.SpawnTimeoutConfig;

import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;

@Mod("spawntimeout")
public class SpawnTimeoutMod {

    public static final String MODID = "spawntimeout";

    public SpawnTimeoutMod() {
        SpawnTimeoutConfig.load();
    }
}
