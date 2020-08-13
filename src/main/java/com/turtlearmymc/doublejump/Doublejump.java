package com.turtlearmymc.doublejump;

import io.netty.buffer.Unpooled;

import java.util.stream.Stream;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.network.ServerSidePacketRegistry;
import net.fabricmc.fabric.api.server.PlayerStream;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.PacketByteBuf;
import net.minecraft.util.registry.Registry;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Doublejump implements ModInitializer {
    public static Logger LOGGER = LogManager.getLogger();

    public static final String MOD_ID = "doublejump";
    public static final String MOD_NAME = "Double Jump Mod";

    public static Enchantment DOUBLE_JUMP;

    public static final Identifier C2S_DOUBLE_JUMP_PACKET_ID = new Identifier("doublejump", "request_effects");
    public static final Identifier S2C_PLAY_EFFECTS_PACKET_ID = new Identifier("doublejump", "play_effects");

    @Override
    public void onInitialize() {
        log(Level.INFO, "Initializing");
        DOUBLE_JUMP = Registry.register(
            Registry.ENCHANTMENT,
            new Identifier(MOD_ID, "doublejump"),
            new EnchantmentDoublejump(Enchantment.Weight.RARE, new EquipmentSlot[]{EquipmentSlot.FEET})
        );

        ServerSidePacketRegistry.INSTANCE.register(C2S_DOUBLE_JUMP_PACKET_ID,
            (packetContext, attachedData) -> {
                PacketByteBuf passedData = new PacketByteBuf(Unpooled.buffer());
                passedData.writeUuid(attachedData.readUuid());

                PlayerEntity player = packetContext.getPlayer();

                packetContext.getTaskQueue().execute(
                    () -> {
                        player.fallDistance = 0;

                        Stream<PlayerEntity> watchingPlayers = PlayerStream.watching(player.getEntityWorld(), player.getBlockPos());

                        watchingPlayers.forEach(
                            p -> {
                                if (p != player) {
                                    ServerSidePacketRegistry.INSTANCE.sendToPlayer(p, Doublejump.S2C_PLAY_EFFECTS_PACKET_ID, passedData);
                                }
                            }
                        );
                    }
                );
            }
        );
    }

    public static void log(Level level, String message){
        LOGGER.log(level, "[" + MOD_NAME + "] " + message);
    }

}