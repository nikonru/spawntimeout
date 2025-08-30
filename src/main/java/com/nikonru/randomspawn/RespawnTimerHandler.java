package com.nikonru.randomspawn;

import com.nikonru.spawntimeout.config.SpawnTimeoutConfig;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.GameType;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerEvent.PlayerLoggedInEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;

@Mod.EventBusSubscriber(modid = "spawntimeout")
public class RespawnTimerHandler {

    private static final String LAST_DEATH_KEY = "SpawnTimeoutLastDeath";
    private static final Map<UUID, ServerPlayer> waitingPlayers = new HashMap<>();

    @SubscribeEvent
    public static void onPlayerDeath(LivingDeathEvent event) {
        if (!(event.getEntity() instanceof ServerPlayer player)) return;

        long now = System.currentTimeMillis();
        player.getPersistentData().putLong(LAST_DEATH_KEY, now);
        player.setGameMode(GameType.SPECTATOR);

        waitingPlayers.put(player.getUUID(), player);
    }

    @SubscribeEvent
    public static void onPlayerLogin(PlayerLoggedInEvent event) {
        if (!(event.getEntity() instanceof ServerPlayer player)) return;

        if (player.getPersistentData().contains(LAST_DEATH_KEY)) {
            long lastDeath = player.getPersistentData().getLong(LAST_DEATH_KEY);
            long respawnDelayMillis = SpawnTimeoutConfig.respawnDelayS.get() * 1000L;
            long remainingMillis = respawnDelayMillis - (System.currentTimeMillis() - lastDeath);

            if (remainingMillis > 0) {
                waitingPlayers.put(player.getUUID(), player);
                player.setGameMode(GameType.SPECTATOR);
            } else {
                player.getPersistentData().remove(LAST_DEATH_KEY);
            }
        }
    }


    @SubscribeEvent
    public static void onServerTick(TickEvent.ServerTickEvent event) {
        if (event.phase != TickEvent.Phase.END) return;

        Iterator<Map.Entry<UUID, ServerPlayer>> iterator = waitingPlayers.entrySet().iterator();
        long now = System.currentTimeMillis();

        while (iterator.hasNext()) {
            Map.Entry<UUID, ServerPlayer> entry = iterator.next();
            ServerPlayer player = entry.getValue();

            long lastDeath = player.getPersistentData().getLong(LAST_DEATH_KEY);
            long respawnDelayMillis = SpawnTimeoutConfig.respawnDelayS.get() * 1000L;
            long remainingMillis = respawnDelayMillis - (now - lastDeath);

            if (remainingMillis > 0) {
                long remainingSeconds = (remainingMillis + 999) / 1000;
                player.displayClientMessage(Component.literal("Respawn in " + remainingSeconds + "s"), true);

                // putting player in "waiting room"
                player.teleportTo(SpawnTimeoutConfig.x.get(), SpawnTimeoutConfig.y.get(), SpawnTimeoutConfig.z.get());
            } else {

                BlockPos respawnPos = player.getRespawnPosition();
                if (respawnPos == null) {
                    respawnPos = player.getCommandSenderWorld().getSharedSpawnPos();
                }

                player.teleportTo(
                        respawnPos.getX() + 0.5,
                        respawnPos.getY(),
                        respawnPos.getZ() + 0.5
                );

                player.setGameMode(GameType.SURVIVAL);
                player.getPersistentData().remove(LAST_DEATH_KEY);
                iterator.remove();
                player.displayClientMessage(Component.literal(""), true);
            }
        }
    }

    @SubscribeEvent
    public static void onPlayerRespawn(PlayerEvent.PlayerRespawnEvent event) {
        if (!(event.getEntity() instanceof ServerPlayer player)) return;

        if (waitingPlayers.containsKey(player.getUUID())) {
            player.setGameMode(GameType.SPECTATOR);
        }
    }
}
