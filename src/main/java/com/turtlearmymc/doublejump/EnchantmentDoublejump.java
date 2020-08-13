package com.turtlearmymc.doublejump;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentTarget;
import net.minecraft.entity.EquipmentSlot;

public class EnchantmentDoublejump extends Enchantment {
    public EnchantmentDoublejump(Enchantment.Weight weight, EquipmentSlot... slotTypes) {
        super(weight, EnchantmentTarget.ARMOR_FEET, slotTypes);
    }

    public int getMinimumPower(int level) {
        return level * 10;
    }

    public int getMaximumPower(int level) {
        return this.getMinimumPower(level) + 15;
    }

    public boolean isTreasure() {
        return true;
    }

    public int getMaximumLevel() {
        return 3;
    }
}