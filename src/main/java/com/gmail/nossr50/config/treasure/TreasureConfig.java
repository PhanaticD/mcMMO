package com.gmail.nossr50.config.treasure;

import com.gmail.nossr50.config.*;
import com.gmail.nossr50.datatypes.treasure.*;
import java.util.*;
import org.bukkit.entity.*;
import org.bukkit.inventory.*;
import org.bukkit.potion.*;
import org.bukkit.material.*;
import org.bukkit.*;
import org.bukkit.configuration.*;
import org.bukkit.inventory.meta.*;
import com.gmail.nossr50.util.*;
import org.bukkit.enchantments.*;

public class TreasureConfig extends ConfigLoader
{
    private static TreasureConfig instance;
    public List<ExcavationTreasure> excavationFromDirt;
    public List<ExcavationTreasure> excavationFromGrass;
    public List<ExcavationTreasure> excavationFromSand;
    public List<ExcavationTreasure> excavationFromGravel;
    public List<ExcavationTreasure> excavationFromClay;
    public List<ExcavationTreasure> excavationFromMycel;
    public List<ExcavationTreasure> excavationFromSoulSand;
    public List<ExcavationTreasure> excavationFromSnow;
    public List<ExcavationTreasure> excavationFromRedSand;
    public List<ExcavationTreasure> excavationFromPodzol;
    public List<HylianTreasure> hylianFromBushes;
    public List<HylianTreasure> hylianFromFlowers;
    public List<HylianTreasure> hylianFromPots;
    public List<ShakeTreasure> shakeFromBlaze;
    public List<ShakeTreasure> shakeFromCaveSpider;
    public List<ShakeTreasure> shakeFromSpider;
    public List<ShakeTreasure> shakeFromChicken;
    public List<ShakeTreasure> shakeFromCow;
    public List<ShakeTreasure> shakeFromCreeper;
    public List<ShakeTreasure> shakeFromEnderman;
    public List<ShakeTreasure> shakeFromGhast;
    public List<ShakeTreasure> shakeFromHorse;
    public List<ShakeTreasure> shakeFromIronGolem;
    public List<ShakeTreasure> shakeFromMagmaCube;
    public List<ShakeTreasure> shakeFromMushroomCow;
    public List<ShakeTreasure> shakeFromPig;
    public List<ShakeTreasure> shakeFromPigZombie;
    public List<ShakeTreasure> shakeFromPlayer;
    public List<ShakeTreasure> shakeFromSheep;
    public List<ShakeTreasure> shakeFromSkeleton;
    public List<ShakeTreasure> shakeFromSlime;
    public List<ShakeTreasure> shakeFromSnowman;
    public List<ShakeTreasure> shakeFromSquid;
    public List<ShakeTreasure> shakeFromWitch;
    public List<ShakeTreasure> shakeFromZombie;
    public HashMap<Rarity, List<FishingTreasure>> fishingRewards;
    public HashMap<Rarity, List<EnchantmentTreasure>> fishingEnchantments;

    private TreasureConfig() {
        super("treasures.yml");
        this.excavationFromDirt = new ArrayList<ExcavationTreasure>();
        this.excavationFromGrass = new ArrayList<ExcavationTreasure>();
        this.excavationFromSand = new ArrayList<ExcavationTreasure>();
        this.excavationFromGravel = new ArrayList<ExcavationTreasure>();
        this.excavationFromClay = new ArrayList<ExcavationTreasure>();
        this.excavationFromMycel = new ArrayList<ExcavationTreasure>();
        this.excavationFromSoulSand = new ArrayList<ExcavationTreasure>();
        this.excavationFromSnow = new ArrayList<ExcavationTreasure>();
        this.excavationFromRedSand = new ArrayList<ExcavationTreasure>();
        this.excavationFromPodzol = new ArrayList<ExcavationTreasure>();
        this.hylianFromBushes = new ArrayList<HylianTreasure>();
        this.hylianFromFlowers = new ArrayList<HylianTreasure>();
        this.hylianFromPots = new ArrayList<HylianTreasure>();
        this.shakeFromBlaze = new ArrayList<ShakeTreasure>();
        this.shakeFromCaveSpider = new ArrayList<ShakeTreasure>();
        this.shakeFromSpider = new ArrayList<ShakeTreasure>();
        this.shakeFromChicken = new ArrayList<ShakeTreasure>();
        this.shakeFromCow = new ArrayList<ShakeTreasure>();
        this.shakeFromCreeper = new ArrayList<ShakeTreasure>();
        this.shakeFromEnderman = new ArrayList<ShakeTreasure>();
        this.shakeFromGhast = new ArrayList<ShakeTreasure>();
        this.shakeFromHorse = new ArrayList<ShakeTreasure>();
        this.shakeFromIronGolem = new ArrayList<ShakeTreasure>();
        this.shakeFromMagmaCube = new ArrayList<ShakeTreasure>();
        this.shakeFromMushroomCow = new ArrayList<ShakeTreasure>();
        this.shakeFromPig = new ArrayList<ShakeTreasure>();
        this.shakeFromPigZombie = new ArrayList<ShakeTreasure>();
        this.shakeFromPlayer = new ArrayList<ShakeTreasure>();
        this.shakeFromSheep = new ArrayList<ShakeTreasure>();
        this.shakeFromSkeleton = new ArrayList<ShakeTreasure>();
        this.shakeFromSlime = new ArrayList<ShakeTreasure>();
        this.shakeFromSnowman = new ArrayList<ShakeTreasure>();
        this.shakeFromSquid = new ArrayList<ShakeTreasure>();
        this.shakeFromWitch = new ArrayList<ShakeTreasure>();
        this.shakeFromZombie = new ArrayList<ShakeTreasure>();
        this.fishingRewards = new HashMap<Rarity, List<FishingTreasure>>();
        this.fishingEnchantments = new HashMap<Rarity, List<EnchantmentTreasure>>();
        this.loadKeys();
        this.validate();
    }

    public static TreasureConfig getInstance() {
        if (TreasureConfig.instance == null) {
            TreasureConfig.instance = new TreasureConfig();
        }
        return TreasureConfig.instance;
    }

    @Override
    protected boolean validateKeys() {
        final List<String> reason = new ArrayList<String>();
        for (final String tier : this.config.getConfigurationSection("Enchantment_Drop_Rates").getKeys(false)) {
            double totalEnchantDropRate = 0.0;
            double totalItemDropRate = 0.0;
            for (final Rarity rarity : Rarity.values()) {
                final double enchantDropRate = this.config.getDouble("Enchantment_Drop_Rates." + tier + "." + rarity.toString());
                final double itemDropRate = this.config.getDouble("Item_Drop_Rates." + tier + "." + rarity.toString());
                if ((enchantDropRate < 0.0 || enchantDropRate > 100.0) && rarity != Rarity.TRAP && rarity != Rarity.RECORD) {
                    reason.add("The enchant drop rate for " + tier + " items that are " + rarity.toString() + "should be between 0.0 and 100.0!");
                }
                if (itemDropRate < 0.0 || itemDropRate > 100.0) {
                    reason.add("The item drop rate for " + tier + " items that are " + rarity.toString() + "should be between 0.0 and 100.0!");
                }
                totalEnchantDropRate += enchantDropRate;
                totalItemDropRate += itemDropRate;
            }
            if (totalEnchantDropRate < 0.0 || totalEnchantDropRate > 100.0) {
                reason.add("The total enchant drop rate for " + tier + " should be between 0.0 and 100.0!");
            }
            if (totalItemDropRate < 0.0 || totalItemDropRate > 100.0) {
                reason.add("The total item drop rate for " + tier + " should be between 0.0 and 100.0!");
            }
        }
        return this.noErrorsInConfig(reason);
    }

    @Override
    protected void loadKeys() {
        if (this.config.getConfigurationSection("Treasures") != null) {
            this.backup();
            return;
        }
        this.loadTreaures("Fishing");
        this.loadTreaures("Excavation");
        this.loadTreaures("Hylian_Luck");
        this.loadEnchantments();
        for (final EntityType entity : EntityType.values()) {
            if (entity.isAlive()) {
                this.loadTreaures("Shake." + entity.toString());
            }
        }
    }

    private void loadTreaures(final String type) {
        final boolean isFishing = type.equals("Fishing");
        final boolean isShake = type.contains("Shake");
        final boolean isExcavation = type.equals("Excavation");
        final boolean isHylian = type.equals("Hylian_Luck");
        final ConfigurationSection treasureSection = this.config.getConfigurationSection(type);
        if (treasureSection == null) {
            return;
        }
        for (final Rarity rarity : Rarity.values()) {
            if (!this.fishingRewards.containsKey(rarity)) {
                this.fishingRewards.put(rarity, new ArrayList<FishingTreasure>());
            }
        }
        for (final String treasureName : treasureSection.getKeys(false)) {
            final List<String> reason = new ArrayList<String>();
            final String[] treasureInfo = treasureName.split("[|]");
            final String materialName = treasureInfo[0];
            Material material;
            if (materialName.contains("POTION")) {
                material = Material.POTION;
            }
            else if (materialName.contains("INK_SACK")) {
                material = Material.INK_SACK;
            }
            else {
                if (materialName.contains("INVENTORY")) {
                    this.shakeFromPlayer.add(new ShakeTreasure(new ItemStack(Material.BED_BLOCK, 1, (short)0), 1, this.getInventoryStealDropChance(), this.getInventoryStealDropLevel()));
                    continue;
                }
                material = Material.matchMaterial(materialName);
            }
            final int amount = this.config.getInt(type + "." + treasureName + ".Amount");
            final short data = (treasureInfo.length == 2) ? Short.parseShort(treasureInfo[1]) : ((short)this.config.getInt(type + "." + treasureName + ".Data"));
            if (material == null) {
                reason.add("Invalid material: " + materialName);
            }
            if (amount <= 0) {
                reason.add("Amount of " + treasureName + " must be greater than 0! " + amount);
            }
            if (material != null && material.isBlock() && (data > 127 || data < -128)) {
                reason.add("Data of " + treasureName + " is invalid! " + data);
            }
            final int xp = this.config.getInt(type + "." + treasureName + ".XP");
            final double dropChance = this.config.getDouble(type + "." + treasureName + ".Drop_Chance");
            final int dropLevel = this.config.getInt(type + "." + treasureName + ".Drop_Level");
            if (xp < 0) {
                reason.add(treasureName + " has an invalid XP value: " + xp);
            }
            if (dropChance < 0.0) {
                reason.add(treasureName + " has an invalid Drop_Chance: " + dropChance);
            }
            if (dropLevel < 0) {
                reason.add(treasureName + " has an invalid Drop_Level: " + dropLevel);
            }
            Rarity rarity2 = null;
            if (isFishing) {
                rarity2 = Rarity.getRarity(this.config.getString(type + "." + treasureName + ".Rarity"));
                if (rarity2 == null) {
                    reason.add("Invalid Rarity for item: " + treasureName);
                }
            }
            ItemStack item = null;
            if (materialName.contains("POTION")) {
                final String potionType = materialName.substring(7);
                try {
                    item = new Potion(PotionType.valueOf(potionType.toUpperCase().trim())).toItemStack(amount);
                    if (this.config.contains(type + "." + treasureName + ".Custom_Name")) {
                        final ItemMeta itemMeta = item.getItemMeta();
                        itemMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', this.config.getString(type + "." + treasureName + ".Custom_Name")));
                        item.setItemMeta(itemMeta);
                    }
                    if (this.config.contains(type + "." + treasureName + ".Lore")) {
                        final ItemMeta itemMeta = item.getItemMeta();
                        final List<String> lore = new ArrayList<String>();
                        for (final String s : this.config.getStringList(type + "." + treasureName + ".Lore")) {
                            lore.add(ChatColor.translateAlternateColorCodes('&', s));
                        }
                        itemMeta.setLore((List)lore);
                        item.setItemMeta(itemMeta);
                    }
                }
                catch (IllegalArgumentException ex) {
                    reason.add("Invalid Potion_Type: " + potionType);
                }
            }
            else if (materialName.contains("INK_SACK")) {
                final String color = materialName.substring(9);
                try {
                    final Dye dye = new Dye();
                    dye.setColor(DyeColor.valueOf(color.toUpperCase().trim()));
                    item = dye.toItemStack(amount);
                    if (this.config.contains(type + "." + treasureName + ".Custom_Name")) {
                        final ItemMeta itemMeta2 = item.getItemMeta();
                        itemMeta2.setDisplayName(ChatColor.translateAlternateColorCodes('&', this.config.getString(type + "." + treasureName + ".Custom_Name")));
                        item.setItemMeta(itemMeta2);
                    }
                    if (this.config.contains(type + "." + treasureName + ".Lore")) {
                        final ItemMeta itemMeta2 = item.getItemMeta();
                        final List<String> lore2 = new ArrayList<String>();
                        for (final String s2 : this.config.getStringList(type + "." + treasureName + ".Lore")) {
                            lore2.add(ChatColor.translateAlternateColorCodes('&', s2));
                        }
                        itemMeta2.setLore((List)lore2);
                        item.setItemMeta(itemMeta2);
                    }
                }
                catch (IllegalArgumentException ex) {
                    reason.add("Invalid Dye_Color: " + color);
                }
            }
            else if (material != null) {
                item = new ItemStack(material, amount, data);
                if (this.config.contains(type + "." + treasureName + ".Custom_Name")) {
                    final ItemMeta itemMeta3 = item.getItemMeta();
                    itemMeta3.setDisplayName(ChatColor.translateAlternateColorCodes('&', this.config.getString(type + "." + treasureName + ".Custom_Name")));
                    item.setItemMeta(itemMeta3);
                }
                if (this.config.contains(type + "." + treasureName + ".Lore")) {
                    final ItemMeta itemMeta3 = item.getItemMeta();
                    final List<String> lore3 = new ArrayList<String>();
                    for (final String s3 : this.config.getStringList(type + "." + treasureName + ".Lore")) {
                        lore3.add(ChatColor.translateAlternateColorCodes('&', s3));
                    }
                    itemMeta3.setLore((List)lore3);
                    item.setItemMeta(itemMeta3);
                }
            }
            if (this.noErrorsInConfig(reason)) {
                if (isFishing) {
                    this.fishingRewards.get(rarity2).add(new FishingTreasure(item, xp));
                }
                else if (isShake) {
                    final ShakeTreasure shakeTreasure = new ShakeTreasure(item, xp, dropChance, dropLevel);
                    if (type.equals("Shake.BLAZE")) {
                        this.shakeFromBlaze.add(shakeTreasure);
                    }
                    else if (type.equals("Shake.CAVE_SPIDER")) {
                        this.shakeFromCaveSpider.add(shakeTreasure);
                    }
                    else if (type.equals("Shake.CHICKEN")) {
                        this.shakeFromChicken.add(shakeTreasure);
                    }
                    else if (type.equals("Shake.COW")) {
                        this.shakeFromCow.add(shakeTreasure);
                    }
                    else if (type.equals("Shake.CREEPER")) {
                        this.shakeFromCreeper.add(shakeTreasure);
                    }
                    else if (type.equals("Shake.ENDERMAN")) {
                        this.shakeFromEnderman.add(shakeTreasure);
                    }
                    else if (type.equals("Shake.GHAST")) {
                        this.shakeFromGhast.add(shakeTreasure);
                    }
                    else if (type.equals("Shake.HORSE")) {
                        this.shakeFromHorse.add(shakeTreasure);
                    }
                    else if (type.equals("Shake.IRON_GOLEM")) {
                        this.shakeFromIronGolem.add(shakeTreasure);
                    }
                    else if (type.equals("Shake.MAGMA_CUBE")) {
                        this.shakeFromMagmaCube.add(shakeTreasure);
                    }
                    else if (type.equals("Shake.MUSHROOM_COW")) {
                        this.shakeFromMushroomCow.add(shakeTreasure);
                    }
                    else if (type.equals("Shake.PIG")) {
                        this.shakeFromPig.add(shakeTreasure);
                    }
                    else if (type.equals("Shake.PIG_ZOMBIE")) {
                        this.shakeFromPigZombie.add(shakeTreasure);
                    }
                    else if (type.equals("Shake.PLAYER")) {
                        this.shakeFromPlayer.add(shakeTreasure);
                    }
                    else if (type.equals("Shake.SHEEP")) {
                        this.shakeFromSheep.add(shakeTreasure);
                    }
                    else if (type.equals("Shake.SKELETON")) {
                        this.shakeFromSkeleton.add(shakeTreasure);
                    }
                    else if (type.equals("Shake.SLIME")) {
                        this.shakeFromSlime.add(shakeTreasure);
                    }
                    else if (type.equals("Shake.SPIDER")) {
                        this.shakeFromSpider.add(shakeTreasure);
                    }
                    else if (type.equals("Shake.SNOWMAN")) {
                        this.shakeFromSnowman.add(shakeTreasure);
                    }
                    else if (type.equals("Shake.SQUID")) {
                        this.shakeFromSquid.add(shakeTreasure);
                    }
                    else if (type.equals("Shake.WITCH")) {
                        this.shakeFromWitch.add(shakeTreasure);
                    }
                    else {
                        if (!type.equals("Shake.ZOMBIE")) {
                            continue;
                        }
                        this.shakeFromZombie.add(shakeTreasure);
                    }
                }
                else if (isExcavation) {
                    final ExcavationTreasure excavationTreasure = new ExcavationTreasure(item, xp, dropChance, dropLevel);
                    final List<String> dropList = (List<String>)this.config.getStringList(type + "." + treasureName + ".Drops_From");
                    if (dropList.contains("Dirt")) {
                        this.excavationFromDirt.add(excavationTreasure);
                    }
                    if (dropList.contains("Grass")) {
                        this.excavationFromGrass.add(excavationTreasure);
                    }
                    if (dropList.contains("Sand")) {
                        this.excavationFromSand.add(excavationTreasure);
                    }
                    if (dropList.contains("Gravel")) {
                        this.excavationFromGravel.add(excavationTreasure);
                    }
                    if (dropList.contains("Clay")) {
                        this.excavationFromClay.add(excavationTreasure);
                    }
                    if (dropList.contains("Mycelium")) {
                        this.excavationFromMycel.add(excavationTreasure);
                    }
                    if (dropList.contains("Soul_Sand")) {
                        this.excavationFromSoulSand.add(excavationTreasure);
                    }
                    if (dropList.contains("Snow")) {
                        this.excavationFromSnow.add(excavationTreasure);
                    }
                    if (dropList.contains("Red_Sand")) {
                        this.excavationFromRedSand.add(excavationTreasure);
                    }
                    if (!dropList.contains("Podzol")) {
                        continue;
                    }
                    this.excavationFromPodzol.add(excavationTreasure);
                }
                else {
                    if (!isHylian) {
                        continue;
                    }
                    final HylianTreasure hylianTreasure = new HylianTreasure(item, xp, dropChance, dropLevel);
                    final List<String> dropList = (List<String>)this.config.getStringList(type + "." + treasureName + ".Drops_From");
                    if (dropList.contains("Bushes")) {
                        this.hylianFromBushes.add(hylianTreasure);
                    }
                    if (dropList.contains("Flowers")) {
                        this.hylianFromFlowers.add(hylianTreasure);
                    }
                    if (!dropList.contains("Pots")) {
                        continue;
                    }
                    this.hylianFromPots.add(hylianTreasure);
                }
            }
        }
    }

    private void loadEnchantments() {
        for (final Rarity rarity : Rarity.values()) {
            if (rarity != Rarity.TRAP) {
                if (rarity != Rarity.RECORD) {
                    if (!this.fishingEnchantments.containsKey(rarity)) {
                        this.fishingEnchantments.put(rarity, new ArrayList<EnchantmentTreasure>());
                    }
                    final ConfigurationSection enchantmentSection = this.config.getConfigurationSection("Enchantments_Rarity." + rarity.toString());
                    if (enchantmentSection == null) {
                        return;
                    }
                    for (final String enchantmentName : enchantmentSection.getKeys(false)) {
                        final int level = this.config.getInt("Enchantments_Rarity." + rarity.toString() + "." + enchantmentName);
                        final Enchantment enchantment = EnchantmentUtils.getByName(enchantmentName);
                        if (enchantment == null) {
                            TreasureConfig.plugin.getLogger().warning("Skipping invalid enchantment in treasures.yml: " + enchantmentName);
                        }
                        else {
                            this.fishingEnchantments.get(rarity).add(new EnchantmentTreasure(enchantment, level));
                        }
                    }
                }
            }
        }
    }

    public boolean getInventoryStealEnabled() {
        return this.config.contains("Shake.PLAYER.INVENTORY");
    }

    public boolean getInventoryStealStacks() {
        return this.config.getBoolean("Shake.PLAYER.INVENTORY.Whole_Stacks");
    }

    public double getInventoryStealDropChance() {
        return this.config.getDouble("Shake.PLAYER.INVENTORY.Drop_Chance");
    }

    public int getInventoryStealDropLevel() {
        return this.config.getInt("Shake.PLAYER.INVENTORY.Drop_Level");
    }

    public double getItemDropRate(final int tier, final Rarity rarity) {
        return this.config.getDouble("Item_Drop_Rates.Tier_" + tier + "." + rarity.toString());
    }

    public double getEnchantmentDropRate(final int tier, final Rarity rarity) {
        return this.config.getDouble("Enchantment_Drop_Rates.Tier_" + tier + "." + rarity.toString());
    }
}
