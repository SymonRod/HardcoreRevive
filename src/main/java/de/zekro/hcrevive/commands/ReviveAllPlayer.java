package de.zekro.hcrevive.commands;


import org.bukkit.entity.Player;
import de.zekro.hcrevive.deathregister.Entry;
import de.zekro.hcrevive.deathregister.DeathRegister;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.Location;
import org.bukkit.GameMode;

/**
 * Command executor for /hcrvReviveAllPlayer command.
 */
public class ReviveAllPlayer implements CommandExecutor {

    private final DeathRegister deathRegister;



    // --- CONFIG VALUES -------------------------
    private final boolean reviveAtDeathPosition;
    // -------------------------------------------


    /**
     * Initialize a new instance of {@link ReviveAllPlayer}.
     * @param deathRegister death register instance
     */
    public ReviveAllPlayer(DeathRegister deathRegister) {
        this.reviveAtDeathPosition = true; // TODO Load value from config
        this.deathRegister = deathRegister;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        // TODO Check permission
        if (!sender.isOp()) { 
            sender.sendMessage(ChatColor.RED + "You don't have the permission to use this command.");
            return false;
        }

        for(Entry entry : this.deathRegister.getAllEntry()) {
            this.revivePlayer(entry);
        }

        this.deathRegister.flush();
        sender.sendMessage(ChatColor.GREEN + "All players have been revived!.");

        return true;
    }


    /**
     * Revive the {@link Player} by the given {@link Entry} instance.
     * @param entry Death Entry
     */
    private void revivePlayer(Entry entry) {
        Player player = entry.getPlayer();

        Location respawnLocation;

        if (this.reviveAtDeathPosition) {
            respawnLocation = entry.getLocation();
        }
        else {
            respawnLocation = player.getBedSpawnLocation();
            if (respawnLocation == null)
                respawnLocation = player.getWorld().getSpawnLocation();
        }

        player.setGameMode(GameMode.SURVIVAL);
        player.teleport(respawnLocation);
        player.sendMessage("You got revived by %s!");
        this.deathRegister.remove(entry);
    }
}
