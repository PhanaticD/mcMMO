package com.gmail.nossr50.datatypes.skills.alchemy;

import java.util.List;

import org.bukkit.Material;
import org.bukkit.potion.Potion;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionType;

public enum PotionStage {
    FIVE(5),
    FOUR(4),
    THREE(3),
    TWO(2),
    ONE(1);

    int numerical;

    private PotionStage(int numerical) {
        this.numerical = numerical;
    }

    public int toNumerical() {
        return numerical;
    }

    private static PotionStage getPotionStageNumerical(int numerical) {
        for (PotionStage potionStage : values()) {
            if (numerical >= potionStage.toNumerical()) {
                return potionStage;
            }
        }

        return ONE;
    }

    public static PotionStage getPotionStage(AlchemyPotion input, AlchemyPotion output) {
        PotionStage potionStage = getPotionStage(output);
        if (!isWaterBottle(input) && getPotionStage(input) == potionStage) {
            potionStage = PotionStage.FIVE;
        }

        return potionStage;
    }

    private static boolean isWaterBottle(AlchemyPotion input) {
        return input.getData() == 0;
    }

    public static PotionStage getPotionStage(final AlchemyPotion alchemyPotion) {
        final Potion potion = alchemyPotion.toPotion(1);
        final List<PotionEffect> effects = alchemyPotion.getEffects();
        int stage = 1;
        if (potion.getType() != null || !effects.isEmpty()) {
            ++stage;
        }
        if (potion.getLevel() > 1) {
            ++stage;
        }
        else if (!effects.isEmpty()) {
            for (final PotionEffect effect : effects) {
                if (effect.getAmplifier() > 0) {
                    ++stage;
                    break;
                }
            }
        }
        if (potion.hasExtendedDuration()) {
            ++stage;
        }
        if (potion.isSplash()) {
            ++stage;
        }
        return getPotionStageNumerical(stage);
    }
}
