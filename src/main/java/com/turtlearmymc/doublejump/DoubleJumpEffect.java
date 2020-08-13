package com.turtlearmymc.doublejump;

import net.minecraft.entity.player.PlayerEntity;

import java.util.Random;

import net.minecraft.particle.ParticleTypes;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.world.World;

public class DoubleJumpEffect {
    private final static Random random = new Random();

    public static void play(PlayerEntity player) {
        play(player, player);
    }

    public static void play(PlayerEntity localPlayer, PlayerEntity effectPlayer) {
        World world = localPlayer.getEntityWorld();
        world.playSound(localPlayer, effectPlayer.getBlockPos(), SoundEvents.ENTITY_TURTLE_SHAMBLE, SoundCategory.PLAYERS, 0.4f, 1);

        for(int i = 0; i < 5; ++i) {
            double d = random.nextGaussian() * 0.02D;
            double e = random.nextGaussian() * 0.02D;
            double f = random.nextGaussian() * 0.02D;
            world.addParticle(ParticleTypes.CLOUD, getParticleX(effectPlayer), effectPlayer.y, getParticleZ(effectPlayer), d, e, f);
        }
    }

    private static double getParticleX(PlayerEntity player) {
        return player.x + (double)(random.nextFloat() * player.getWidth() * 2.0F) - (double)player.getWidth();
    }

    private static double getParticleZ(PlayerEntity player) {
        return player.z + (double)(random.nextFloat() * player.getWidth() * 2.0F) - (double)player.getWidth();
    }

}