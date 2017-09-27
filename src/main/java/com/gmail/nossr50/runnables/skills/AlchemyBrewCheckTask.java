package com.gmail.nossr50.runnables.skills;

import java.util.Arrays;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.BlockState;
import org.bukkit.block.BrewingStand;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import com.gmail.nossr50.skills.alchemy.Alchemy;
import com.gmail.nossr50.skills.alchemy.AlchemyPotionBrewer;

public class AlchemyBrewCheckTask extends BukkitRunnable {
    private Player player;
    private BrewingStand brewingStand;
    private ItemStack[] oldInventory;

    public AlchemyBrewCheckTask(Player player, BrewingStand brewingStand) {
        this.player = player;
        this.brewingStand = brewingStand;
        this.oldInventory = Arrays.copyOfRange(brewingStand.getInventory().getContents(), 0, 4);
    }

    public void run() {
        final Location location = this.brewingStand.getLocation();
        final ItemStack[] newInventory = Arrays.copyOfRange(this.brewingStand.getInventory().getContents(), 0, 4);
        final boolean validBrew = AlchemyPotionBrewer.isValidBrew(this.player, newInventory);
        if (Alchemy.brewingStandMap.containsKey(location)) {
            if (this.oldInventory[3] == null || newInventory[3] == null || !this.oldInventory[3].isSimilar(newInventory[3]) || !validBrew) {
                Alchemy.brewingStandMap.get(location).cancelBrew();
            }
        }
        else if (validBrew) {
            Alchemy.brewingStandMap.put(location, new AlchemyBrewTask((BlockState)this.brewingStand, this.player));
        }
    }
}
