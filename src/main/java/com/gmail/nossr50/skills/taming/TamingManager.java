package com.gmail.nossr50.skills.taming;

import com.gmail.nossr50.skills.*;
import com.gmail.nossr50.datatypes.player.*;
import org.bukkit.permissions.*;
import com.gmail.nossr50.datatypes.skills.*;
import com.gmail.nossr50.runnables.skills.*;
import com.gmail.nossr50.locale.*;
import com.gmail.nossr50.events.skills.secondaryabilities.*;
import com.gmail.nossr50.*;
import org.bukkit.event.*;
import com.gmail.nossr50.util.skills.*;
import com.gmail.nossr50.util.player.*;
import com.gmail.nossr50.util.*;
import com.gmail.nossr50.events.fake.*;
import org.bukkit.metadata.*;
import org.bukkit.entity.*;
import com.gmail.nossr50.config.*;
import org.bukkit.inventory.*;
import org.bukkit.*;
import java.util.*;

public class TamingManager extends SkillManager
{
    private static HashMap<EntityType, List<TrackedTamingEntity>> summonedEntities;

    public TamingManager(final McMMOPlayer mcMMOPlayer) {
        super(mcMMOPlayer, SkillType.TAMING);
    }

    public boolean canUseThickFur() {
        return this.getSkillLevel() >= Taming.thickFurUnlockLevel && Permissions.secondaryAbilityEnabled((Permissible)this.getPlayer(), SecondaryAbility.THICK_FUR);
    }

    public boolean canUseEnvironmentallyAware() {
        return this.getSkillLevel() >= Taming.environmentallyAwareUnlockLevel && Permissions.secondaryAbilityEnabled((Permissible)this.getPlayer(), SecondaryAbility.ENVIROMENTALLY_AWARE);
    }

    public boolean canUseShockProof() {
        return this.getSkillLevel() >= Taming.shockProofUnlockLevel && Permissions.secondaryAbilityEnabled((Permissible)this.getPlayer(), SecondaryAbility.SHOCK_PROOF);
    }

    public boolean canUseHolyHound() {
        return this.getSkillLevel() >= Taming.holyHoundUnlockLevel && Permissions.secondaryAbilityEnabled((Permissible)this.getPlayer(), SecondaryAbility.HOLY_HOUND);
    }

    public boolean canUseFastFoodService() {
        return this.getSkillLevel() >= Taming.fastFoodServiceUnlockLevel && Permissions.secondaryAbilityEnabled((Permissible)this.getPlayer(), SecondaryAbility.FAST_FOOD);
    }

    public boolean canUseSharpenedClaws() {
        return this.getSkillLevel() >= Taming.sharpenedClawsUnlockLevel && Permissions.secondaryAbilityEnabled((Permissible)this.getPlayer(), SecondaryAbility.SHARPENED_CLAWS);
    }

    public boolean canUseGore() {
        return Permissions.secondaryAbilityEnabled((Permissible)this.getPlayer(), SecondaryAbility.GORE);
    }

    public boolean canUseBeastLore() {
        return Permissions.secondaryAbilityEnabled((Permissible)this.getPlayer(), SecondaryAbility.BEAST_LORE);
    }

    public void awardTamingXP(final LivingEntity entity) {
        switch (entity.getType()) {
            case HORSE: {
                this.applyXpGain(Taming.horseXp, XPGainReason.PVE);
            }
            case WOLF: {
                this.applyXpGain(Taming.wolfXp, XPGainReason.PVE);
            }
            case OCELOT: {
                this.applyXpGain(Taming.ocelotXp, XPGainReason.PVE);
            }
            default: {}
        }
    }

    public void fastFoodService(final Wolf wolf, final double damage) {
        if (!SkillUtils.activationSuccessful(SecondaryAbility.FAST_FOOD, this.getPlayer(), Taming.fastFoodServiceActivationChance, this.activationChance)) {
            return;
        }
        final double health = wolf.getHealth();
        final double maxHealth = wolf.getMaxHealth();
        if (health < maxHealth) {
            final double newHealth = health + damage;
            wolf.setHealth(Math.min(newHealth, maxHealth));
        }
    }

    public double gore(final LivingEntity target, double damage) {
        if (!SkillUtils.activationSuccessful(SecondaryAbility.GORE, this.getPlayer(), this.getSkillLevel(), this.activationChance)) {
            return 0.0;
        }
        BleedTimerTask.add(target, Taming.goreBleedTicks);
        if (target instanceof Player) {
            ((Player)target).sendMessage(LocaleLoader.getString("Combat.StruckByGore"));
        }
        this.getPlayer().sendMessage(LocaleLoader.getString("Combat.Gore"));
        damage = damage * Taming.goreModifier - damage;
        return damage;
    }

    public double sharpenedClaws() {
        return Taming.sharpenedClawsBonusDamage;
    }

    public void summonOcelot() {
        if (!Permissions.callOfTheWild((Permissible)this.getPlayer(), EntityType.OCELOT)) {
            return;
        }
        this.callOfTheWild(EntityType.OCELOT, Config.getInstance().getTamingCOTWCost(EntityType.OCELOT));
    }

    public void summonWolf() {
        if (!Permissions.callOfTheWild((Permissible)this.getPlayer(), EntityType.WOLF)) {
            return;
        }
        this.callOfTheWild(EntityType.WOLF, Config.getInstance().getTamingCOTWCost(EntityType.WOLF));
    }

    public void summonHorse() {
        if (!Permissions.callOfTheWild((Permissible)this.getPlayer(), EntityType.HORSE)) {
            return;
        }
        this.callOfTheWild(EntityType.HORSE, Config.getInstance().getTamingCOTWCost(EntityType.HORSE));
    }

    public void beastLore(final LivingEntity target) {
        final Player player = this.getPlayer();
        final Tameable beast = (Tameable)target;
        String message = LocaleLoader.getString("Combat.BeastLore") + " ";
        if (beast.isTamed() && beast.getOwner() != null) {
            message = message.concat(LocaleLoader.getString("Combat.BeastLoreOwner", beast.getOwner().getName()) + " ");
        }
        message = message.concat(LocaleLoader.getString("Combat.BeastLoreHealth", target.getHealth(), target.getMaxHealth()));
        player.sendMessage(message);
    }

    public void processEnvironmentallyAware(final Wolf wolf, final double damage) {
        if (damage > wolf.getHealth()) {
            return;
        }
        final Player owner = this.getPlayer();
        wolf.teleport((Entity)owner);
        owner.sendMessage(LocaleLoader.getString("Taming.Listener.Wolf"));
    }

    public void pummel(final LivingEntity target, final Wolf wolf) {
        final double chance = 10 / this.activationChance;
        final SecondaryAbilityWeightedActivationCheckEvent event = new SecondaryAbilityWeightedActivationCheckEvent(this.getPlayer(), SecondaryAbility.PUMMEL, chance);
        mcMMO.p.getServer().getPluginManager().callEvent((Event)event);
        if (event.getChance() * this.activationChance <= Misc.getRandom().nextInt(this.activationChance)) {
            return;
        }
        ParticleEffectUtils.playGreaterImpactEffect(target);
        target.setVelocity(wolf.getLocation().getDirection().normalize().multiply(1.5));
        if (target instanceof Player) {
            final Player defender = (Player)target;
            if (UserManager.getPlayer(defender).useChatNotifications()) {
                defender.sendMessage("Wolf pummeled at you");
            }
        }
    }

    public void attackTarget(final LivingEntity target) {
        final double range = 5.0;
        final Player player = this.getPlayer();
        for (final Entity entity : player.getNearbyEntities(range, range, range)) {
            if (entity.getType() != EntityType.WOLF) {
                continue;
            }
            final Wolf wolf = (Wolf)entity;
            if (!wolf.isTamed() || wolf.getOwner() != player) {
                continue;
            }
            if (wolf.isSitting()) {
                continue;
            }
            wolf.setTarget(target);
        }
    }

    private void callOfTheWild(final EntityType type, final int summonAmount) {
        final Player player = this.getPlayer();
        final ItemStack heldItem = player.getItemInHand();
        final int heldItemAmount = heldItem.getAmount();
        Location location = player.getLocation();
        if (heldItemAmount < summonAmount) {
            player.sendMessage(LocaleLoader.getString("Skills.NeedMore", StringUtils.getPrettyItemString(heldItem.getType())));
            return;
        }
        if (!this.rangeCheck(type)) {
            return;
        }
        final int amount = Config.getInstance().getTamingCOTWAmount(type);
        final int tamingCOTWLength = Config.getInstance().getTamingCOTWLength(type);
        for (int i = 0; i < amount; ++i) {
            if (!this.summonAmountCheck(type)) {
                return;
            }
            location = Misc.getLocationOffset(location, 1.0);
            final LivingEntity entity = (LivingEntity)player.getWorld().spawnEntity(location, type);
            final FakeEntityTameEvent event = new FakeEntityTameEvent(entity, (AnimalTamer)player);
            mcMMO.p.getServer().getPluginManager().callEvent((Event)event);
            if (!event.isCancelled()) {
                entity.setMetadata("mcMMO: Spawned Entity", (MetadataValue)mcMMO.metadataValue);
                ((Tameable)entity).setOwner((AnimalTamer)player);
                entity.setRemoveWhenFarAway(false);
                addToTracker(entity);
                switch (type) {
                    case OCELOT: {
                        ((Ocelot)entity).setCatType(Ocelot.Type.values()[1 + Misc.getRandom().nextInt(3)]);
                        break;
                    }
                    case WOLF: {
                        entity.setMaxHealth(20.0);
                        entity.setHealth(entity.getMaxHealth());
                        break;
                    }
                    case HORSE: {
                        final Horse horse = (Horse)entity;
                        entity.setMaxHealth(15.0 + Misc.getRandom().nextDouble() * 15.0);
                        entity.setHealth(entity.getMaxHealth());
                        horse.setColor(Horse.Color.values()[Misc.getRandom().nextInt(Horse.Color.values().length)]);
                        horse.setStyle(Horse.Style.values()[Misc.getRandom().nextInt(Horse.Style.values().length)]);
                        horse.setJumpStrength(Math.max(AdvancedConfig.getInstance().getMinHorseJumpStrength(), Math.min(Math.min(Misc.getRandom().nextDouble(), Misc.getRandom().nextDouble()) * 2.0, AdvancedConfig.getInstance().getMaxHorseJumpStrength())));
                        break;
                    }
                }
                if (Permissions.renamePets((Permissible)player)) {
                    entity.setCustomName(LocaleLoader.getString("Taming.Summon.Name.Format", player.getName(), StringUtils.getPrettyEntityTypeString(type)));
                }
                ParticleEffectUtils.playCallOfTheWildEffect(entity);
            }
        }
        player.setItemInHand((heldItemAmount == summonAmount) ? null : new ItemStack(heldItem.getType(), heldItemAmount - summonAmount));
        String lifeSpan = "";
        if (tamingCOTWLength > 0) {
            lifeSpan = LocaleLoader.getString("Taming.Summon.Lifespan", tamingCOTWLength);
        }
        player.sendMessage(LocaleLoader.getString("Taming.Summon.Complete") + lifeSpan);
        player.playSound(location, Sound.FIREWORK_LARGE_BLAST2, 1.0f, 0.5f);
    }

    private boolean rangeCheck(final EntityType type) {
        final double range = Config.getInstance().getTamingCOTWRange();
        final Player player = this.getPlayer();
        if (range == 0.0) {
            return true;
        }
        for (final Entity entity : player.getNearbyEntities(range, range, range)) {
            if (entity.getType() == type) {
                player.sendMessage(Taming.getCallOfTheWildFailureMessage(type));
                return false;
            }
        }
        return true;
    }

    private boolean summonAmountCheck(final EntityType entityType) {
        final Player player = this.getPlayer();
        final int maxAmountSummons = Config.getInstance().getTamingCOTWMaxAmount(entityType);
        if (maxAmountSummons <= 0) {
            return true;
        }
        final List<TrackedTamingEntity> trackedEntities = getTrackedEntities(entityType);
        final int summonAmount = (trackedEntities == null) ? 0 : trackedEntities.size();
        if (summonAmount >= maxAmountSummons) {
            player.sendMessage(LocaleLoader.getString("Taming.Summon.Fail.TooMany", maxAmountSummons));
            return false;
        }
        return true;
    }

    protected static void addToTracker(final LivingEntity livingEntity) {
        final TrackedTamingEntity trackedEntity = new TrackedTamingEntity(livingEntity);
        if (!TamingManager.summonedEntities.containsKey(livingEntity.getType())) {
            TamingManager.summonedEntities.put(livingEntity.getType(), new ArrayList<TrackedTamingEntity>());
        }
        TamingManager.summonedEntities.get(livingEntity.getType()).add(trackedEntity);
    }

    protected static List<TrackedTamingEntity> getTrackedEntities(final EntityType entityType) {
        return TamingManager.summonedEntities.get(entityType);
    }

    protected static void removeFromTracker(final TrackedTamingEntity trackedEntity) {
        TamingManager.summonedEntities.get(trackedEntity.getLivingEntity().getType()).remove(trackedEntity);
    }

    static {
        TamingManager.summonedEntities = new HashMap<EntityType, List<TrackedTamingEntity>>();
    }
}
