package com.nikonru.spawntimeout.config;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.config.ModConfig;

public class SpawnTimeoutConfig {
    public static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
    private static ForgeConfigSpec SPEC;

    // Config values
    public static ForgeConfigSpec.IntValue respawnDelayS;
    public static ForgeConfigSpec.IntValue x;
    public static ForgeConfigSpec.IntValue y;
    public static ForgeConfigSpec.IntValue z;

    public static void load() {
        BUILDER.comment("SpawnTimeout mod settings").push("spawn_timeout");

        respawnDelayS = BUILDER
                .comment("Respawn delay in seconds")
                .defineInRange("respawnDelayS", 10, 1, 100*24*60*60);
        

        int MAX = 1000000;
        x = BUILDER
                .comment("waiting position x coordinate")
                .defineInRange("x", 0, -MAX, MAX);
        
        y = BUILDER
                .comment("waiting position y coordinate")
                .defineInRange("y", 0, 0, MAX);

        z = BUILDER
                .comment("waiting position z coordinate")
                .defineInRange("z", 0, -MAX, MAX);

        BUILDER.pop();
        SPEC = BUILDER.build();

        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, SPEC, "SpawnTimeout.toml");
    }
}
