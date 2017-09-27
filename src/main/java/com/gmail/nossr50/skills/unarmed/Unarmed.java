package com.gmail.nossr50.skills.unarmed;

import java.util.Iterator;

import org.bukkit.entity.Item;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import com.gmail.nossr50.config.AdvancedConfig;
import com.gmail.nossr50.config.Config;

public class Unarmed {
    public static double ironArmMinBonusDamage = AdvancedConfig.getInstance().getIronArmMinBonus();
    public static double ironArmMaxBonusDamage = AdvancedConfig.getInstance().getIronArmMaxBonus();
    public static int    ironArmIncreaseLevel  = AdvancedConfig.getInstance().getIronArmIncreaseLevel();

    public static boolean blockCrackerSmoothBrick = Config.getInstance().getUnarmedBlockCrackerSmoothbrickToCracked();

    public static double berserkDamageModifier = 1.5;

    public static boolean handleItemPickup(final PlayerInventory inventory, final Item drop) {
        final ItemStack dropStack = drop.getItemStack();
        final int firstEmpty = inventory.firstEmpty();
        int dropAmount = dropStack.getAmount();
        if (inventory.containsAtLeast(dropStack, 1)) {
            int nextSlot = 0;
            for (final ItemStack itemstack : inventory) {
                if (dropStack.isSimilar(itemstack)) {
                    final int itemAmount = itemstack.getAmount();
                    final int itemMax = itemstack.getMaxStackSize();
                    final ItemStack addStack = itemstack.clone();
                    if (dropAmount + itemAmount <= itemMax) {
                        drop.remove();
                        addStack.setAmount(dropAmount + itemAmount);
                        inventory.setItem(nextSlot, addStack);
                        return true;
                    }
                    addStack.setAmount(itemMax);
                    dropAmount = dropAmount + itemAmount - itemMax;
                    inventory.setItem(nextSlot, addStack);
                }
                if (dropAmount == 0) {
                    drop.remove();
                    return true;
                }
                ++nextSlot;
            }
        }
        if (firstEmpty == inventory.getHeldItemSlot()) {
            int nextSlot = firstEmpty + 1;
            final Iterator<ItemStack> iterator = (Iterator<ItemStack>)inventory.iterator(nextSlot);
            while (iterator.hasNext()) {
                final ItemStack itemstack = iterator.next();
                if (itemstack == null) {
                    drop.remove();
                    dropStack.setAmount(dropAmount);
                    inventory.setItem(nextSlot, dropStack);
                    return true;
                }
                ++nextSlot;
            }
            if (dropStack.getAmount() == dropAmount) {
                return false;
            }
            drop.remove();
            dropStack.setAmount(dropAmount);
            drop.getWorld().dropItem(drop.getLocation(), dropStack).setPickupDelay(0);
            return true;
        }
        else {
            if (firstEmpty != -1) {
                drop.remove();
                dropStack.setAmount(dropAmount);
                inventory.setItem(firstEmpty, dropStack);
                return true;
            }
            drop.remove();
            return true;
        }
    }
}
