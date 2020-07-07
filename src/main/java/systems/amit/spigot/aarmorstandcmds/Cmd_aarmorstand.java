package systems.amit.spigot.aarmorstandcmds;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class Cmd_aarmorstand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Player only command");
            return false;
        }
        Player p = (Player) sender;
        if (!p.isOp() && !p.hasPermission(aArmorStandCmds.getInstance().ADMIN_PERM)) {
            p.sendMessage(ChatColor.RED + "No Permission");
            return false;
        }

        List<Entity> nearbyEntities = p.getNearbyEntities(1, 0, 0);
        Location armorstandLoc = null;

        for (Entity e : nearbyEntities) {
            if (e.getType().equals(EntityType.ARMOR_STAND)) {
                armorstandLoc = new Location(e.getWorld(), e.getLocation().getBlockX(), e.getLocation().getBlockY(), e.getLocation().getBlockZ());
                break;
            }
        }

        if (armorstandLoc == null) {
            p.sendMessage(ChatColor.RED + "You must be close to an armor stand!");
            return false;
        }

        if (args.length == 0) {
            p.sendMessage(ChatColor.RED + "Usage: /" + label + " <add/remove/list> [command]");
            return false;
        }

        if (args[0].equalsIgnoreCase("add")) {
            if (args.length < 2) {
                p.sendMessage(ChatColor.RED + "Usage: /" + label + " add <command>");
                return false;
            }
            if (!aArmorStandCmds.getInstance().armorstandMap.containsKey(armorstandLoc)) {
                aArmorStandCmds.getInstance().armorstandMap.put(armorstandLoc, new ArrayList<>());
            }

            String input = "";
            for (int i = 1; i < args.length; i++) {
                input += args[i] + " ";
            }
            input = input.trim();

            if (aArmorStandCmds.getInstance().armorstandMap.get(armorstandLoc).contains(input)) {
                p.sendMessage(ChatColor.RED + "The armor stand already has the command!");
                return false;
            }

            aArmorStandCmds.getInstance().armorstandMap.get(armorstandLoc).add(input);
            aArmorStandCmds.getInstance().getConfig().set("data." + armorstandLoc.getWorld().getName() + "." + armorstandLoc.getBlockX() + "," + armorstandLoc.getBlockY() + "," + armorstandLoc.getBlockZ(),
                    aArmorStandCmds.getInstance().armorstandMap.get(armorstandLoc));
            aArmorStandCmds.getInstance().saveConfig();
            p.sendMessage(ChatColor.GREEN + "The command has been added to the armor stand.");
        } else if (args[0].equalsIgnoreCase("remove")) {
            if (!aArmorStandCmds.getInstance().armorstandMap.containsKey(armorstandLoc)) {
                p.sendMessage(ChatColor.RED + "The armor stand has no commands listed.");
                return false;
            }

            if (args.length < 2) {
                p.sendMessage(ChatColor.RED + "Usage: /" + label + " remove <command>");
                return false;
            }

            String input = "";
            for (int i = 1; i < args.length; i++) {
                input += args[i];
            }
            input = input.trim();

            if (!aArmorStandCmds.getInstance().armorstandMap.get(armorstandLoc).contains(input)) {
                p.sendMessage(ChatColor.RED + "The armor stand does not have the command!");
                return false;
            }

            aArmorStandCmds.getInstance().armorstandMap.get(armorstandLoc).remove(input);
            aArmorStandCmds.getInstance().getConfig().set("data." + armorstandLoc.getWorld().getName() + "." + armorstandLoc.getBlockX() + "," + armorstandLoc.getBlockY() + "," + armorstandLoc.getBlockZ(),
                    aArmorStandCmds.getInstance().armorstandMap.get(armorstandLoc));
            aArmorStandCmds.getInstance().saveConfig();
            p.sendMessage(ChatColor.GREEN + "The command has been removed from the armor stand.");
        } else if (args[0].equalsIgnoreCase("list")) {
            if (!aArmorStandCmds.getInstance().armorstandMap.containsKey(armorstandLoc)) {
                p.sendMessage(ChatColor.RED + "The armor stand has no commands listed.");
                return false;
            }
            p.sendMessage(ChatColor.GREEN + "Command list:");
            for (String asCmd : aArmorStandCmds.getInstance().armorstandMap.get(armorstandLoc)) {
                p.sendMessage(ChatColor.GRAY + asCmd);
            }
        } else {
            p.sendMessage(ChatColor.RED + "Unknown sub command");
        }

        return false;
    }
}
