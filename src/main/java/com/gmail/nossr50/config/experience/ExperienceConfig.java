package com.gmail.nossr50.config.experience;

import com.gmail.nossr50.config.*;
import com.gmail.nossr50.datatypes.experience.*;
import com.gmail.nossr50.datatypes.skills.alchemy.*;
import com.gmail.nossr50.util.*;

import java.util.*;

import org.bukkit.entity.*;
import org.bukkit.material.*;
import org.bukkit.*;
import com.gmail.nossr50.datatypes.skills.*;

public class ExperienceConfig extends AutoUpdateConfigLoader {

	private static ExperienceConfig instance;

	private ExperienceConfig() {
		super("experience.yml");
		this.validate();
	}

	public static ExperienceConfig getInstance() {
		if (ExperienceConfig.instance == null) {
			ExperienceConfig.instance = new ExperienceConfig();
		}
		return ExperienceConfig.instance;
	}

	@Override
	protected void loadKeys() {
	}

	@Override
	protected boolean validateKeys() {
		final List<String> reason = new ArrayList<String>();
		if (this.getMultiplier(FormulaType.EXPONENTIAL) <= 0.0) {
			reason.add("Experience_Formula.Exponential_Values.multiplier should be greater than 0!");
		}
		if (this.getMultiplier(FormulaType.LINEAR) <= 0.0) {
			reason.add("Experience_Formula.Linear_Values.multiplier should be greater than 0!");
		}
		if (this.getExponent(FormulaType.EXPONENTIAL) <= 0.0) {
			reason.add("Experience_Formula.Exponential_Values.exponent should be greater than 0!");
		}
		if (this.getExperienceGainsGlobalMultiplier() <= 0.0) {
			reason.add("Experience_Formula.Multiplier.Global should be greater than 0!");
		}
		if (this.getPlayerVersusPlayerXP() < 0.0) {
			reason.add("Experience_Formula.Multiplier.PVP should be at least 0!");
		}
		if (this.getSpawnedMobXpMultiplier() < 0.0) {
			reason.add("Experience_Formula.Mobspawners.Multiplier should be at least 0!");
		}
		if (this.getBredMobXpMultiplier() < 0.0) {
			reason.add("Experience_Formula.Breeding.Multiplier should be at least 0!");
		}
		if (this.getExpModifier() <= 0.0) {
			reason.add("Conversion.Exp_Modifier should be greater than 0!");
		}
		for (final PotionStage potionStage : PotionStage.values()) {
			if (this.getPotionXP(potionStage) < 0.0) {
				reason.add("Experience.Alchemy.Potion_Stage_" + potionStage.toNumerical() + " should be at least 0!");
			}
		}
		if (this.getArcheryDistanceMultiplier() < 0.0) {
			reason.add("Experience.Archery.Distance_Multiplier should be at least 0!");
		}
		if (this.getAnimalsXP() < 0.0) {
			reason.add("Experience.Combat.Multiplier.Animals should be at least 0!");
		}
		if (this.getWitherSkeletonXP() < 0.0) {
			reason.add("Experience.Combat.Multiplier.Wither_Skeleton should be at least 0!");
		}
		if (this.getDodgeXPModifier() < 0) {
			reason.add("Skills.Acrobatics.Dodge_XP_Modifier should be at least 0!");
		}
		if (this.getRollXPModifier() < 0) {
			reason.add("Skills.Acrobatics.Roll_XP_Modifier should be at least 0!");
		}
		if (this.getFallXPModifier() < 0) {
			reason.add("Skills.Acrobatics.Fall_XP_Modifier should be at least 0!");
		}
		if (this.getFishingShakeXP() <= 0) {
			reason.add("Experience.Fishing.Shake should be greater than 0!");
		}
		if (this.getRepairXPBase() <= 0.0) {
			reason.add("Experience.Repair.Base should be greater than 0!");
		}
		if (this.getTamingXPWolf() <= 0) {
			reason.add("Experience.Taming.Animal_Taming.Wolf should be greater than 0!");
		}
		if (this.getTamingXPOcelot() <= 0) {
			reason.add("Experience.Taming.Animal_Taming.Ocelot should be greater than 0!");
		}
		for (final TreeSpecies species : TreeSpecies.values()) {
			final String key = "Experience.Woodcutting." + StringUtils.getPrettyTreeSpeciesString(species).replace(" ", "_");
			if (this.config.getInt(key) <= 0) {
				reason.add(key + " should be greater than 0!");
			}
		}
		if (this.getWoodcuttingXPHugeBrownMushroom() <= 0) {
			reason.add("Experience.Woodcutting.Huge_Mushroom_Brown should be greater than 0!");
		}
		if (this.getWoodcuttingXPHugeRedMushroom() <= 0) {
			reason.add("Experience.Woodcutting.Huge_Mushroom_Red should be greater than 0!");
		}
		return this.noErrorsInConfig(reason);
	}

	public FormulaType getFormulaType() {
		return FormulaType.getFormulaType(this.config.getString("Experience_Formula.Curve"));
	}

	public boolean getCumulativeCurveEnabled() {
		return this.config.getBoolean("Experience_Formula.Cumulative_Curve", false);
	}

	public double getMultiplier(final FormulaType type) {
		return this.config.getDouble("Experience_Formula." + StringUtils.getCapitalized(type.toString()) + "_Values.multiplier");
	}

	public int getBase(final FormulaType type) {
		return this.config.getInt("Experience_Formula." + StringUtils.getCapitalized(type.toString()) + "_Values.base");
	}

	public double getExponent(final FormulaType type) {
		return this.config.getDouble("Experience_Formula." + StringUtils.getCapitalized(type.toString()) + "_Values.exponent");
	}

	public double getExperienceGainsGlobalMultiplier() {
		return this.config.getDouble("Experience_Formula.Multiplier.Global", 1.0);
	}

	public void setExperienceGainsGlobalMultiplier(final double value) {
		this.config.set("Experience_Formula.Multiplier.Global", (Object) value);
	}

	public double getPlayerVersusPlayerXP() {
		return this.config.getDouble("Experience_Formula.Multiplier.PVP", 1.0);
	}

	public double getSpawnedMobXpMultiplier() {
		return this.config.getDouble("Experience_Formula.Mobspawners.Multiplier", 0.0);
	}

	public double getBredMobXpMultiplier() {
		return this.config.getDouble("Experience_Formula.Breeding.Multiplier", 1.0);
	}

	public double getFormulaSkillModifier(final SkillType skill) {
		return this.config.getDouble("Experience_Formula.Modifier." + StringUtils.getCapitalized(skill.toString()));
	}

	public double getCustomXpPerkBoost() {
		return this.config.getDouble("Experience_Formula.Custom_XP_Perk.Boost", 1.25);
	}

	public boolean getDiminishedReturnsEnabled() {
		return this.config.getBoolean("Diminished_Returns.Enabled", false);
	}

	public int getDiminishedReturnsThreshold(final SkillType skill) {
		return this.config.getInt("Diminished_Returns.Threshold." + StringUtils.getCapitalized(skill.toString()), 20000);
	}

	public int getDiminishedReturnsTimeInterval() {
		return this.config.getInt("Diminished_Returns.Time_Interval", 10);
	}

	public double getExpModifier() {
		return this.config.getDouble("Conversion.Exp_Modifier", 1.0);
	}

	public boolean getExperienceGainsPlayerVersusPlayerEnabled() {
		return this.config.getBoolean("Experience.PVP.Rewards", true);
	}

	public double getCombatXP(final EntityType entity) {
		return this.config.getDouble("Experience.Combat.Multiplier." + StringUtils.getPrettyEntityTypeString(entity).replace(" ", "_"));
	}

	public double getAnimalsXP() {
		return this.config.getDouble("Experience.Combat.Multiplier.Animals", 1.0);
	}

	public double getWitherSkeletonXP() {
		return this.config.getDouble("Experience.Combat.Multiplier.Wither_Skeleton", 4.0);
	}

	public double getElderGuardianXP() {
		return this.config.getDouble("Experience.Combat.Multiplier.Elder_Guardian", 4.0);
	}

	public int getXp(final SkillType skill, final Material material) {
		return this.config.getInt("Experience." + StringUtils.getCapitalized(skill.toString()) + "." + StringUtils.getPrettyItemString(material).replace(" ", "_"));
	}

	public int getDodgeXPModifier() {
		return this.config.getInt("Experience.Acrobatics.Dodge", 120);
	}

	public int getRollXPModifier() {
		return this.config.getInt("Experience.Acrobatics.Roll", 80);
	}

	public int getFallXPModifier() {
		return this.config.getInt("Experience.Acrobatics.Fall", 120);
	}

	public double getFeatherFallXPModifier() {
		return this.config.getDouble("Experience.Acrobatics.FeatherFall_Multiplier", 2.0);
	}

	public double getPotionXP(final PotionStage stage) {
		return this.config.getDouble("Experience.Alchemy.Potion_Stage_" + stage.toNumerical(), 10.0);
	}

	public double getArcheryDistanceMultiplier() {
		return this.config.getDouble("Experience.Archery.Distance_Multiplier", 0.025);
	}

	public int getDirtAndSandXp(final MaterialData data) {
		final Material type = data.getItemType();
		if (type == Material.DIRT) {
			switch (data.getData()) {
				case 0: {
					return this.config.getInt("Experience.Excavation.Dirt", 40);
				}
				case 1: {
					return this.config.getInt("Experience.Excavation.Coarse_Dirt", 40);
				}
				case 2: {
					return this.config.getInt("Experience.Excavation.Podzol", 40);
				}
				default: {
					return 0;
				}
			}
		}
		else {
			if (type != Material.SAND) {
				return 0;
			}
			switch (data.getData()) {
				case 0: {
					return this.config.getInt("Experience.Excavation.Sand", 40);
				}
				case 1: {
					return this.config.getInt("Experience.Excavation.Red_Sand", 40);
				}
				default: {
					return 0;
				}
			}
		}
	}

	public int getFishXp(final MaterialData data) {
		switch (data.getData()) {
			case 0: {
				return this.config.getInt("Experience.Fishing.Raw_Fish", 800);
			}
			case 1: {
				return this.config.getInt("Experience.Fishing.Raw_Salmon", 800);
			}
			case 2: {
				return this.config.getInt("Experience.Fishing.Clownfish", 800);
			}
			case 3: {
				return this.config.getInt("Experience.Fishing.Pufferfish", 800);
			}
			default: {
				return 0;
			}
		}
	}

	public int getFishingShakeXP() {
		return this.config.getInt("Experience.Fishing.Shake", 50);
	}

	public int getFlowerAndGrassXp(final MaterialData data) {
		final Material type = data.getItemType();
		if (type == Material.RED_ROSE) {
			switch (data.getData()) {
				case 0: {
					return this.config.getInt("Experience.Herbalism.Poppy", 100);
				}
				case 1: {
					return this.config.getInt("Experience.Herbalism.Blue_Orchid", 150);
				}
				case 2: {
					return this.config.getInt("Experience.Herbalism.Allium", 300);
				}
				case 3: {
					return this.config.getInt("Experience.Herbalism.Azure_Bluet", 150);
				}
				case 4: {
					return this.config.getInt("Experience.Herbalism.Red_Tulip", 150);
				}
				case 5: {
					return this.config.getInt("Experience.Herbalism.Orange_Tulip", 150);
				}
				case 6: {
					return this.config.getInt("Experience.Herbalism.White_Tulip", 150);
				}
				case 7: {
					return this.config.getInt("Experience.Herbalism.Pink_Tulip", 150);
				}
				case 8: {
					return this.config.getInt("Experience.Herbalism.Oxeye_Daisy", 150);
				}
				default: {
					return 0;
				}
			}
		}
		else if (type == Material.LONG_GRASS) {
			final GrassSpecies species = ((LongGrass) data).getSpecies();
			if (species == null) {
				return 0;
			}
			switch (species) {
				case DEAD: {
					return this.config.getInt("Experience.Herbalism.Dead_Bush", 30);
				}
				case FERN_LIKE: {
					return this.config.getInt("Experience.Herbalism.Small_Fern", 10);
				}
				case NORMAL: {
					return this.config.getInt("Experience.Herbalism.Small_Grass", 10);
				}
				default: {
					return 0;
				}
			}
		}
		else {
			if (type != Material.DOUBLE_PLANT) {
				return 0;
			}
			switch (data.getData()) {
				case 0: {
					return this.config.getInt("Experience.Herbalism.Sunflower", 50);
				}
				case 1: {
					return this.config.getInt("Experience.Herbalism.Lilac", 50);
				}
				case 2: {
					return this.config.getInt("Experience.Herbalism.Tall_Grass", 50);
				}
				case 3: {
					return this.config.getInt("Experience.Herbalism.Tall_Fern", 50);
				}
				case 4: {
					return this.config.getInt("Experience.Herbalism.Rose_Bush", 50);
				}
				case 5: {
					return this.config.getInt("Experience.Herbalism.Peony", 50);
				}
				default: {
					return 0;
				}
			}
		}
	}

	public double getRepairXPBase() {
		return this.config.getDouble("Experience.Repair.Base", 1000.0);
	}

	public double getRepairXP(final MaterialType repairMaterialType) {
		return this.config.getDouble("Experience.Repair." + StringUtils.getCapitalized(repairMaterialType.toString()));
	}

	public int getTamingXPHorse() {
		return this.config.getInt("Experience.Taming.Animal_Taming.Horse", 1000);
	}

	public int getTamingXPWolf() {
		return this.config.getInt("Experience.Taming.Animal_Taming.Wolf", 250);
	}

	public int getTamingXPOcelot() {
		return this.config.getInt("Experience.Taming.Animal_Taming.Ocelot", 500);
	}

	public int getWoodcuttingTreeXP(final TreeSpecies species) {
		return this.config.getInt("Experience.Woodcutting." + StringUtils.getPrettyTreeSpeciesString(species).replace(" ", "_"));
	}

	public int getWoodcuttingXPHugeBrownMushroom() {
		return this.config.getInt("Experience.Woodcutting.Huge_Mushroom_Brown", 70);
	}

	public int getWoodcuttingXPHugeRedMushroom() {
		return this.config.getInt("Experience.Woodcutting.Huge_Mushroom_Red", 70);
	}
}
