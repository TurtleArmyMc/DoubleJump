package com.turtlearmymc.doublejump;

import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import io.netty.buffer.Unpooled;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;

public class Doublejump implements ModInitializer {
    public static Logger LOGGER = LogManager.getLogger();

    public static final String MOD_ID = "doublejump";
    public static final String MOD_NAME = "Double Jump Mod";

    public static Enchantment DOUBLE_JUMP;

    public static final Identifier C2S_DO_DOUBLEJUMP = new Identifier("doublejump", "request_effects");
    public static final Identifier S2C_PLAY_EFFECTS_PACKET_ID = new Identifier("doublejump", "play_effects");

    @Override
    public void onInitialize() {
        log(Level.INFO, "Initializing");
        DOUBLE_JUMP = Registry.register(Registries.ENCHANTMENT, new Identifier(MOD_ID, "doublejump"),
                new EnchantmentDoublejump(Enchantment.Rarity.RARE, new EquipmentSlot[] { EquipmentSlot.FEET }));

        ServerPlayNetworking.registerGlobalReceiver(C2S_DO_DOUBLEJUMP,
                (server, player, handler, buf, responseSender) -> {
                    PacketByteBuf passedData = new PacketByteBuf(Unpooled.buffer());
                    passedData.writeUuid(buf.readUuid());

                    server.execute(() -> {
                        PlayerLookup.tracking(player).forEach(p -> {
                            if (p != player) {
                                ServerPlayNetworking.send(p, Doublejump.S2C_PLAY_EFFECTS_PACKET_ID, passedData);
                            }
                        });
                    });
                });
    }

    public static void log(Level level, String message) {
        LOGGER.log(level, "[" + MOD_NAME + "] " + message);
    }

}