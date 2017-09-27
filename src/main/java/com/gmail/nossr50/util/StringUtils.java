package com.gmail.nossr50.util;

import org.bukkit.entity.*;
import org.bukkit.*;
import com.gmail.nossr50.datatypes.skills.*;
import com.gmail.nossr50.datatypes.party.*;

public class StringUtils
{
    public static String getCapitalized(final String target) {
        return target.substring(0, 1).toUpperCase() + target.substring(1).toLowerCase();
    }

    public static String getPrettyItemString(final Material material) {
        return createPrettyEnumString(material.toString());
    }

    public static String getPrettyEntityTypeString(final EntityType entity) {
        return createPrettyEnumString(entity.toString());
    }

    public static String getPrettyAbilityString(final AbilityType ability) {
        return createPrettyEnumString(ability.toString());
    }

    public static String getPrettyTreeSpeciesString(final TreeSpecies species) {
        return createPrettyEnumString(species.toString());
    }

    public static String getPrettySecondaryAbilityString(final SecondaryAbility secondaryAbility) {
        switch (secondaryAbility) {
            case HERBALISM_DOUBLE_DROPS:
            case MINING_DOUBLE_DROPS:
            case WOODCUTTING_DOUBLE_DROPS: {
                return "Double Drops";
            }
            case FISHING_TREASURE_HUNTER:
            case EXCAVATION_TREASURE_HUNTER: {
                return "Treasure Hunter";
            }
            case GREEN_THUMB_BLOCK:
            case GREEN_THUMB_PLANT: {
                return "Green Thumb";
            }
            default: {
                return createPrettyEnumString(secondaryAbility.toString());
            }
        }
    }

    public static String getPrettyPartyFeatureString(final PartyFeature partyFeature) {
        return createPrettyEnumString(partyFeature.toString());
    }

    private static String createPrettyEnumString(final String baseString) {
        final String[] substrings = baseString.split("_");
        String prettyString = "";
        int size = 1;
        for (final String string : substrings) {
            prettyString = prettyString.concat(getCapitalized(string));
            if (size < substrings.length) {
                prettyString = prettyString.concat(" ");
            }
            ++size;
        }
        return prettyString;
    }

    public static boolean isInt(final String string) {
        try {
            Integer.parseInt(string);
            return true;
        }
        catch (NumberFormatException nFE) {
            return false;
        }
    }

    public static boolean isDouble(final String string) {
        try {
            Double.parseDouble(string);
            return true;
        }
        catch (NumberFormatException nFE) {
            return false;
        }
    }
}
