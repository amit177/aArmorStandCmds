package systems.amit.spigot.aarmorstandcmds;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;

import java.util.List;

public class Event_PlayerInteractAtEntity implements Listener {

    @EventHandler
    public void onInteract(PlayerInteractAtEntityEvent e) {
        if (e.getRightClicked() == null || !e.getRightClicked().getType().equals(EntityType.ARMOR_STAND)) return;

        if (aArmorStandCmds.getInstance().protectArmorStands) {
            e.setCancelled(true);
        }

        ArmorStand as = (ArmorStand) e.getRightClicked();
        Location loc = new Location(as.getLocation().getWorld(), as.getLocation().getBlockX(), as.getLocation().getBlockY(), as.getLocation().getBlockZ());

        if (aArmorStandCmds.getInstance().armorstandMap.containsKey(loc) && aArmorStandCmds.getInstance().armorstandMap.get(loc).size() > 0) {
            e.setCancelled(true);
            List<String> cmdList = aArmorStandCmds.getInstance().armorstandMap.get(loc);
            for (String cmd : cmdList) {
                cmd = cmd.replaceAll("%player%", e.getPlayer().getName());

                if (cmd.startsWith("server ") && aArmorStandCmds.getInstance().bungeeSupport) {
                    aArmorStandCmds.getInstance().bungeeSend(e.getPlayer(), cmd.replace("server ", ""));
                } else {
                    e.getPlayer().chat("/" + cmd);
                }
            }
        }
    }
}
