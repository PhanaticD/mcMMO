package com.gmail.nossr50.skills.taming;

import org.bukkit.*;
import org.bukkit.entity.*;
import com.gmail.nossr50.locale.*;
import com.gmail.nossr50.config.*;
import com.gmail.nossr50.config.experience.*;

public class Taming
{
    public static int environmentallyAwareUnlockLevel;
    public static int holyHoundUnlockLevel;
    public static int fastFoodServiceUnlockLevel;
    public static double fastFoodServiceActivationChance;
    public static int goreBleedTicks;
    public static double goreModifier;
    public static int sharpenedClawsUnlockLevel;
    public static double sharpenedClawsBonusDamage;
    public static int shockProofUnlockLevel;
    public static double shockProofModifier;
    public static int thickFurUnlockLevel;
    public static double thickFurModifier;
    public static int wolfXp;
    public static int ocelotXp;
    public static int horseXp;

    public static boolean canPreventDamage(final Tameable pet, final AnimalTamer owner) {
        return pet.isTamed() && owner instanceof Player && pet instanceof Wolf;
    }

    public static double processThickFur(final Wolf wolf, final double damage) {
        wolf.playEffect(EntityEffect.WOLF_SHAKE);
        return damage / Taming.thickFurModifier;
    }

    public static void processThickFurFire(final Wolf wolf) {
        wolf.playEffect(EntityEffect.WOLF_SMOKE);
        wolf.setFireTicks(0);
    }

    public static double processShockProof(final Wolf wolf, final double damage) {
        wolf.playEffect(EntityEffect.WOLF_SHAKE);
        return damage / Taming.shockProofModifier;
    }

    public static void processHolyHound(final Wolf wolf, final double damage) {
        final double modifiedHealth = Math.min(wolf.getHealth() + damage, wolf.getMaxHealth());
        wolf.setHealth(modifiedHealth);
        wolf.playEffect(EntityEffect.WOLF_HEARTS);
    }

    protected static String getCallOfTheWildFailureMessage(final EntityType type) {
        switch (type) {
            case OCELOT: {
                return LocaleLoader.getString("Taming.Summon.Fail.Ocelot");
            }
            case WOLF: {
                return LocaleLoader.getString("Taming.Summon.Fail.Wolf");
            }
            case HORSE: {
                return LocaleLoader.getString("Taming.Summon.Fail.Horse");
            }
            default: {
                return "";
            }
        }
    }

    static {
        Taming.environmentallyAwareUnlockLevel = AdvancedConfig.getInstance().getEnviromentallyAwareUnlock();
        Taming.holyHoundUnlockLevel = AdvancedConfig.getInstance().getHolyHoundUnlock();
        Taming.fastFoodServiceUnlockLevel = AdvancedConfig.getInstance().getFastFoodUnlock();
        Taming.fastFoodServiceActivationChance = AdvancedConfig.getInstance().getFastFoodChance();
        Taming.goreBleedTicks = AdvancedConfig.getInstance().getGoreBleedTicks();
        Taming.goreModifier = AdvancedConfig.getInstance().getGoreModifier();
        Taming.sharpenedClawsUnlockLevel = AdvancedConfig.getInstance().getSharpenedClawsUnlock();
        Taming.sharpenedClawsBonusDamage = AdvancedConfig.getInstance().getSharpenedClawsBonus();
        Taming.shockProofUnlockLevel = AdvancedConfig.getInstance().getShockProofUnlock();
        Taming.shockProofModifier = AdvancedConfig.getInstance().getShockProofModifier();
        Taming.thickFurUnlockLevel = AdvancedConfig.getInstance().getThickFurUnlock();
        Taming.thickFurModifier = AdvancedConfig.getInstance().getThickFurModifier();
        Taming.wolfXp = ExperienceConfig.getInstance().getTamingXPWolf();
        Taming.ocelotXp = ExperienceConfig.getInstance().getTamingXPOcelot();
        Taming.horseXp = ExperienceConfig.getInstance().getTamingXPHorse();
    }
}
