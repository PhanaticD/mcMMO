package com.gmail.nossr50.config.skills.alchemy;

import com.gmail.nossr50.config.*;
import org.bukkit.inventory.*;
import com.gmail.nossr50.datatypes.skills.alchemy.*;
import org.bukkit.configuration.*;
import java.util.*;
import com.gmail.nossr50.*;
import org.bukkit.potion.*;
import org.bukkit.*;

public class PotionConfig extends ConfigLoader
{
    private static PotionConfig instance;
    private List<ItemStack> concoctionsIngredientsTierOne;
    private List<ItemStack> concoctionsIngredientsTierTwo;
    private List<ItemStack> concoctionsIngredientsTierThree;
    private List<ItemStack> concoctionsIngredientsTierFour;
    private List<ItemStack> concoctionsIngredientsTierFive;
    private List<ItemStack> concoctionsIngredientsTierSix;
    private List<ItemStack> concoctionsIngredientsTierSeven;
    private List<ItemStack> concoctionsIngredientsTierEight;
    private Map<Short, AlchemyPotion> potionMap;

    private PotionConfig() {
        super("potions.yml");
        this.concoctionsIngredientsTierOne = new ArrayList<ItemStack>();
        this.concoctionsIngredientsTierTwo = new ArrayList<ItemStack>();
        this.concoctionsIngredientsTierThree = new ArrayList<ItemStack>();
        this.concoctionsIngredientsTierFour = new ArrayList<ItemStack>();
        this.concoctionsIngredientsTierFive = new ArrayList<ItemStack>();
        this.concoctionsIngredientsTierSix = new ArrayList<ItemStack>();
        this.concoctionsIngredientsTierSeven = new ArrayList<ItemStack>();
        this.concoctionsIngredientsTierEight = new ArrayList<ItemStack>();
        this.potionMap = new HashMap<Short, AlchemyPotion>();
        this.loadKeys();
    }

    public static PotionConfig getInstance() {
        if (PotionConfig.instance == null) {
            PotionConfig.instance = new PotionConfig();
        }
        return PotionConfig.instance;
    }

    @Override
    protected void loadKeys() {
        this.loadConcoctions();
        this.loadPotionMap();
    }

    private void loadConcoctions() {
        final ConfigurationSection concoctionSection = this.config.getConfigurationSection("Concoctions");
        this.loadConcoctionsTier(this.concoctionsIngredientsTierOne, concoctionSection.getStringList("Tier_One_Ingredients"));
        this.loadConcoctionsTier(this.concoctionsIngredientsTierTwo, concoctionSection.getStringList("Tier_Two_Ingredients"));
        this.loadConcoctionsTier(this.concoctionsIngredientsTierThree, concoctionSection.getStringList("Tier_Three_Ingredients"));
        this.loadConcoctionsTier(this.concoctionsIngredientsTierFour, concoctionSection.getStringList("Tier_Four_Ingredients"));
        this.loadConcoctionsTier(this.concoctionsIngredientsTierFive, concoctionSection.getStringList("Tier_Five_Ingredients"));
        this.loadConcoctionsTier(this.concoctionsIngredientsTierSix, concoctionSection.getStringList("Tier_Six_Ingredients"));
        this.loadConcoctionsTier(this.concoctionsIngredientsTierSeven, concoctionSection.getStringList("Tier_Seven_Ingredients"));
        this.loadConcoctionsTier(this.concoctionsIngredientsTierEight, concoctionSection.getStringList("Tier_Eight_Ingredients"));
        this.concoctionsIngredientsTierTwo.addAll(this.concoctionsIngredientsTierOne);
        this.concoctionsIngredientsTierThree.addAll(this.concoctionsIngredientsTierTwo);
        this.concoctionsIngredientsTierFour.addAll(this.concoctionsIngredientsTierThree);
        this.concoctionsIngredientsTierFive.addAll(this.concoctionsIngredientsTierFour);
        this.concoctionsIngredientsTierSix.addAll(this.concoctionsIngredientsTierFive);
        this.concoctionsIngredientsTierSeven.addAll(this.concoctionsIngredientsTierSix);
        this.concoctionsIngredientsTierEight.addAll(this.concoctionsIngredientsTierSeven);
    }

    private void loadConcoctionsTier(final List<ItemStack> ingredientList, final List<String> ingredientStrings) {
        if (ingredientStrings != null && ingredientStrings.size() > 0) {
            for (final String ingredientString : ingredientStrings) {
                final ItemStack ingredient = this.loadIngredient(ingredientString);
                if (ingredient != null) {
                    ingredientList.add(ingredient);
                }
            }
        }
    }

    private void loadPotionMap() {
        final ConfigurationSection potionSection = this.config.getConfigurationSection("Potions");
        int pass = 0;
        int fail = 0;
        for (final String dataValue : potionSection.getKeys(false)) {
            final AlchemyPotion potion = this.loadPotion(potionSection.getConfigurationSection(dataValue));
            if (potion != null) {
                this.potionMap.put(potion.getData(), potion);
                ++pass;
            }
            else {
                ++fail;
            }
        }
        mcMMO.p.debug("Loaded " + pass + " Alchemy potions, skipped " + fail + ".");
    }

    private AlchemyPotion loadPotion(final ConfigurationSection potion_section) {
        try {
            final short dataValue = Short.parseShort(potion_section.getName());
            String name = potion_section.getString("Name");
            if (name != null) {
                name = ChatColor.translateAlternateColorCodes('&', name);
            }
            final List<String> lore = new ArrayList<String>();
            if (potion_section.contains("Lore")) {
                for (final String line : potion_section.getStringList("Lore")) {
                    lore.add(ChatColor.translateAlternateColorCodes('&', line));
                }
            }
            final List<PotionEffect> effects = new ArrayList<PotionEffect>();
            if (potion_section.contains("Effects")) {
                for (final String effect : potion_section.getStringList("Effects")) {
                    final String[] parts = effect.split(" ");
                    final PotionEffectType type = (parts.length > 0) ? PotionEffectType.getByName(parts[0]) : null;
                    final int amplifier = (parts.length > 1) ? Integer.parseInt(parts[1]) : 0;
                    final int duration = (parts.length > 2) ? Integer.parseInt(parts[2]) : 0;
                    if (type != null) {
                        effects.add(new PotionEffect(type, duration, amplifier));
                    }
                    else {
                        mcMMO.p.getLogger().warning("Failed to parse effect for potion " + name + ": " + effect);
                    }
                }
            }
            final Map<ItemStack, Short> children = new HashMap<ItemStack, Short>();
            if (potion_section.contains("Children")) {
                for (final String child : potion_section.getConfigurationSection("Children").getKeys(false)) {
                    final ItemStack ingredient = this.loadIngredient(child);
                    if (ingredient != null) {
                        children.put(ingredient, Short.parseShort(potion_section.getConfigurationSection("Children").getString(child)));
                    }
                    else {
                        mcMMO.p.getLogger().warning("Failed to parse child for potion " + name + ": " + child);
                    }
                }
            }
            return new AlchemyPotion(dataValue, name, lore, effects, children);
        }
        catch (Exception e) {
            mcMMO.p.getLogger().warning("Failed to load Alchemy potion: " + potion_section.getName());
            return null;
        }
    }

    private ItemStack loadIngredient(final String ingredient) {
        if (ingredient == null || ingredient.isEmpty()) {
            return null;
        }
        final String[] parts = ingredient.split(":");
        final Material material = (parts.length > 0) ? Material.getMaterial(parts[0]) : null;
        final short data = (short)((parts.length > 1) ? Short.parseShort(parts[1]) : 0);
        if (material != null) {
            return new ItemStack(material, 1, data);
        }
        return null;
    }

    public List<ItemStack> getIngredients(final int tier) {
        switch (tier) {
            case 8: {
                return this.concoctionsIngredientsTierEight;
            }
            case 7: {
                return this.concoctionsIngredientsTierSeven;
            }
            case 6: {
                return this.concoctionsIngredientsTierSix;
            }
            case 5: {
                return this.concoctionsIngredientsTierFive;
            }
            case 4: {
                return this.concoctionsIngredientsTierFour;
            }
            case 3: {
                return this.concoctionsIngredientsTierThree;
            }
            case 2: {
                return this.concoctionsIngredientsTierTwo;
            }
            default: {
                return this.concoctionsIngredientsTierOne;
            }
        }
    }

    public boolean isValidPotion(final ItemStack item) {
        return this.potionMap.containsKey(item.getDurability());
    }

    public AlchemyPotion getPotion(final short durability) {
        return this.potionMap.get(durability);
    }
}
