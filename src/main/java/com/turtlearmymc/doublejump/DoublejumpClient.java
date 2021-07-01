package com.turtlearmymc.doublejump;

import java.util.UUID;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.entity.player.PlayerEntity;

public class DoublejumpClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        ClientPlayNetworking.registerGlobalReceiver(Doublejump.S2C_PLAY_EFFECTS_PACKET_ID,
                (client, handler, buf, responseSender) -> {
                    UUID effectPlayerUuid = buf.readUuid();
                    client.execute(() -> {
                        PlayerEntity effectPlayer = client.player.getEntityWorld().getPlayerByUuid(effectPlayerUuid);
                        if (effectPlayer != null) {
                            DoubleJumpEffect.play(client.player, effectPlayer);
                        }
                    });
                });
    }
}