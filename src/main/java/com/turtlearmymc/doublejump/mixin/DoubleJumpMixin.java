package com.turtlearmymc.doublejump.mixin;

import com.turtlearmymc.doublejump.Doublejump;
import com.turtlearmymc.doublejump.DoubleJumpEffect;

import io.netty.buffer.Unpooled;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.fabricmc.fabric.api.network.ClientSidePacketRegistry;

import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.ElytraItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.PacketByteBuf;

@Mixin(ClientPlayerEntity.class)
public abstract class DoubleJumpMixin {
    private int jumpCount = 0;
    private boolean jumpedLastTick = false;

    @Inject(method="tickMovement", at = @At("HEAD"))
    private void tickMovement(CallbackInfo info) {
        ClientPlayerEntity player = (ClientPlayerEntity) (Object) this;
        if (player.onGround || player.isClimbing()) {
            jumpCount = EnchantmentHelper.getEquipmentLevel(Doublejump.DOUBLE_JUMP, player);
        } else if (!jumpedLastTick && jumpCount > 0 && player.getVelocity().y < 0) {
            if (player.input.jumping && !player.abilities.flying) {
                if (canJump(player)) {
                    --jumpCount;
                    player.jump();

                    DoubleJumpEffect.play(player);

                    PacketByteBuf passedData = new PacketByteBuf(Unpooled.buffer());
                    passedData.writeUuid(player.getUuid());

                    ClientSidePacketRegistry.INSTANCE.sendToServer(Doublejump.C2S_PLAY_EFFECTS_REQUEST_PACKET_ID, passedData);
                }
            }
        }
        jumpedLastTick = player.input.jumping;
    }

    private boolean wearingUsableElytra(ClientPlayerEntity player) {
        ItemStack chestItemStack = player.getEquippedStack(EquipmentSlot.CHEST);
        return chestItemStack.getItem() == Items.ELYTRA && ElytraItem.isUsable(chestItemStack);
    }

    private boolean canJump(ClientPlayerEntity player) {
        return !wearingUsableElytra(player) && !player.isFallFlying() && !player.hasVehicle() && !player.isTouchingWater() && !player.hasStatusEffect(StatusEffects.LEVITATION);
    }
}