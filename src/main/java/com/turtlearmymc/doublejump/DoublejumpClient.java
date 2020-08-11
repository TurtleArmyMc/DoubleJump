package com.turtlearmymc.doublejump;

import java.util.UUID;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.network.ClientSidePacketRegistry;
import net.minecraft.entity.player.PlayerEntity;

public class DoublejumpClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        ClientSidePacketRegistry.INSTANCE.register(Doublejump.S2C_PLAY_EFFECTS_PACKET_ID,
            (packetContext, attachedData) -> {
                PlayerEntity localPlayer = packetContext.getPlayer();
                UUID effectPlayerUuid = attachedData.readUuid();
                packetContext.getTaskQueue().execute(
                () -> {
                        PlayerEntity effectPlayer = localPlayer.getEntityWorld().getPlayerByUuid(effectPlayerUuid);
                        if (effectPlayer != null) {
                            DoubleJumpEffect.play(localPlayer, effectPlayer);
                        }
                    }
                );
            }
        );
    }
}