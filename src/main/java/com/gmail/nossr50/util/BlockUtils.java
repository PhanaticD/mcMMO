package com.gmail.nossr50.util;

import org.bukkit.block.*;
import com.gmail.nossr50.*;
import org.bukkit.*;
import org.bukkit.material.*;
import com.gmail.nossr50.skills.repair.*;
import com.gmail.nossr50.skills.salvage.*;
import java.util.*;

public final class BlockUtils
{
    public static boolean shouldBeWatched(final BlockState blockState) {
        return affectedByGigaDrillBreaker(blockState) || affectedByGreenTerra(blockState) || affectedBySuperBreaker(blockState) || isLog(blockState);
    }

    public static boolean canActivateAbilities(final BlockState blockState) {
        switch (blockState.getType()) {
            case BED_BLOCK:
            case BREWING_STAND:
            case BOOKSHELF:
            case BURNING_FURNACE:
            case CAKE_BLOCK:
            case CHEST:
            case DISPENSER:
            case ENCHANTMENT_TABLE:
            case ENDER_CHEST:
            case FENCE_GATE:
            case FURNACE:
            case IRON_DOOR_BLOCK:
            case JUKEBOX:
            case LEVER:
            case NOTE_BLOCK:
            case STONE_BUTTON:
            case WOOD_BUTTON:
            case TRAP_DOOR:
            case WALL_SIGN:
            case WOODEN_DOOR:
            case WORKBENCH:
            case BEACON:
            case ANVIL:
            case DROPPER:
            case HOPPER:
            case TRAPPED_CHEST:
            case IRON_DOOR:
            case IRON_TRAPDOOR:
            case ACACIA_DOOR:
            case SPRUCE_DOOR:
            case BIRCH_DOOR:
            case JUNGLE_DOOR:
            case DARK_OAK_DOOR:
            case ACACIA_FENCE:
            case DARK_OAK_FENCE:
            case BIRCH_FENCE:
            case JUNGLE_FENCE:
            case ARMOR_STAND: {
                return false;
            }
            default: {
                return !isMcMMOAnvil(blockState) && !mcMMO.getModManager().isCustomAbilityBlock(blockState);
            }
        }
    }

    public static boolean isOre(final BlockState blockState) {
        return MaterialUtils.isOre(blockState.getData());
    }

    public static boolean canMakeMossy(final BlockState blockState) {
        switch (blockState.getType()) {
            case COBBLESTONE:
            case DIRT: {
                return true;
            }
            case SMOOTH_BRICK: {
                return ((SmoothBrick)blockState.getData()).getMaterial() == Material.STONE;
            }
            case COBBLE_WALL: {
                return blockState.getRawData() == 0;
            }
            default: {
                return false;
            }
        }
    }

    public static boolean affectedByGreenTerra(final BlockState blockState) {
        switch (blockState.getType()) {
            case BROWN_MUSHROOM:
            case CACTUS:
            case DOUBLE_PLANT:
            case MELON_BLOCK:
            case LONG_GRASS:
            case PUMPKIN:
            case RED_MUSHROOM:
            case RED_ROSE:
            case SUGAR_CANE_BLOCK:
            case VINE:
            case WATER_LILY:
            case YELLOW_FLOWER: {
                return true;
            }
            case CARROT:
            case POTATO: {
                return blockState.getRawData() == CropState.RIPE.getData();
            }
            case CROPS: {
                return ((Crops)blockState.getData()).getState() == CropState.RIPE;
            }
            case NETHER_WARTS: {
                return ((NetherWarts)blockState.getData()).getState() == NetherWartsState.RIPE;
            }
            case COCOA: {
                return ((CocoaPlant)blockState.getData()).getSize() == CocoaPlant.CocoaPlantSize.LARGE;
            }
            default: {
                return mcMMO.getModManager().isCustomHerbalismBlock(blockState);
            }
        }
    }

    public static Boolean affectedBySuperBreaker(final BlockState blockState) {
        switch (blockState.getType()) {
            case ENDER_STONE:
            case GLOWSTONE:
            case HARD_CLAY:
            case MOSSY_COBBLESTONE:
            case NETHERRACK:
            case OBSIDIAN:
            case PACKED_ICE:
            case SANDSTONE:
            case STAINED_CLAY:
            case STONE:
            case PRISMARINE:
            case RED_SANDSTONE: {
                return true;
            }
            default: {
                return isOre(blockState) || mcMMO.getModManager().isCustomMiningBlock(blockState);
            }
        }
    }

    public static boolean affectedByGigaDrillBreaker(final BlockState blockState) {
        switch (blockState.getType()) {
            case DIRT:
            case CLAY:
            case GRASS:
            case GRAVEL:
            case MYCEL:
            case SAND:
            case SNOW:
            case SNOW_BLOCK:
            case SOUL_SAND: {
                return true;
            }
            default: {
                return mcMMO.getModManager().isCustomExcavationBlock(blockState);
            }
        }
    }

    public static boolean isLog(final BlockState blockState) {
        switch (blockState.getType()) {
            case LOG:
            case LOG_2:
            case HUGE_MUSHROOM_1:
            case HUGE_MUSHROOM_2: {
                return true;
            }
            default: {
                return mcMMO.getModManager().isCustomLog(blockState);
            }
        }
    }

    public static boolean isLeaves(final BlockState blockState) {
        switch (blockState.getType()) {
            case LEAVES:
            case LEAVES_2: {
                return true;
            }
            default: {
                return mcMMO.getModManager().isCustomLeaf(blockState);
            }
        }
    }

    public static boolean affectedByFluxMining(final BlockState blockState) {
        switch (blockState.getType()) {
            case IRON_ORE:
            case GOLD_ORE: {
                return true;
            }
            default: {
                return false;
            }
        }
    }

    public static boolean canActivateHerbalism(final BlockState blockState) {
        switch (blockState.getType()) {
            case DIRT:
            case GRASS:
            case SOIL: {
                return false;
            }
            default: {
                return true;
            }
        }
    }

    public static boolean affectedByBlockCracker(final BlockState blockState) {
        switch (blockState.getType()) {
            case SMOOTH_BRICK: {
                return ((SmoothBrick)blockState.getData()).getMaterial() == Material.STONE;
            }
            default: {
                return false;
            }
        }
    }

    public static boolean canMakeShroomy(final BlockState blockState) {
        switch (blockState.getType()) {
            case DIRT:
            case GRASS: {
                return true;
            }
            default: {
                return false;
            }
        }
    }

    public static boolean isMcMMOAnvil(final BlockState blockState) {
        final Material type = blockState.getType();
        return type == Repair.anvilMaterial || type == Salvage.anvilMaterial;
    }

    public static boolean isPistonPiece(final BlockState blockState) {
        final Material type = blockState.getType();
        return type == Material.PISTON_MOVING_PIECE || type == Material.AIR;
    }

    public static HashSet<Byte> getTransparentBlocks() {
        final HashSet<Byte> transparentBlocks = new HashSet<Byte>();
        for (final Material material : Material.values()) {
            if (material.isTransparent()) {
                transparentBlocks.add((byte)material.getId());
            }
        }
        return transparentBlocks;
    }
}
