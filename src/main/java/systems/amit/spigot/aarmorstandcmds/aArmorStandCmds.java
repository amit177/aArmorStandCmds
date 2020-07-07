package systems.amit.spigot.aarmorstandcmds;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import org.bstats.bukkit.Metrics;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.List;

public class aArmorStandCmds extends JavaPlugin {

    HashMap<Location, List<String>> armorstandMap = new HashMap<>();
    boolean bungeeSupport = false;
    boolean protectArmorStands = false;
    private static aArmorStandCmds instance;
    final String ADMIN_PERM = "aarmorstandcmds.admin";

    @Override
    public void onEnable() {
        instance = this;
        if (!loadConfig()) {
            getLogger().severe("Could not load config, disabling.");
            Bukkit.getPluginManager().disablePlugin(this);
            return;
        }

        if (bungeeSupport) {
            Bukkit.getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
        }

        getServer().getPluginManager().registerEvents(new Event_PlayerInteractAtEntity(), this);
        getCommand("aarmorstand").setExecutor(new Cmd_aarmorstand());
        new Metrics(this, 8110);

        getLogger().info("The plugin has been enabled v" + getDescription().getVersion());
    }

    @Override
    public void onDisable() {
        armorstandMap.clear();
        getLogger().info("The plugin has been disabled");
        instance = null;
    }

    private boolean loadConfig() {
        saveDefaultConfig();
        if (!getConfig().contains("data")) {
            getLogger().severe("The config is missing the 'data' section");
            return false;
        }
        if (!getConfig().contains("bungee-support")) {
            getLogger().severe("The config is missing the 'bungee-support' section");
            return false;
        }
        if (!getConfig().contains("protect-armorstands")) {
            getLogger().severe("The config is missing the 'protect-armorstands' section");
            return false;
        }

        bungeeSupport = getConfig().getBoolean("bungee-support");
        protectArmorStands = getConfig().getBoolean("protect-armorstands");
        if (getConfig().isConfigurationSection("data")) {
            for (String worldName : getConfig().getConfigurationSection("data").getKeys(false)) {
                for (String locStr : getConfig().getConfigurationSection("data." + worldName).getKeys(false)) {
                    String[] locList = locStr.split(",");
                    try {
                        Location loc = new Location(Bukkit.getWorld(worldName), Double.parseDouble(locList[0]), Double.parseDouble(locList[1]), Double.parseDouble(locList[2]));
                        armorstandMap.put(loc, getConfig().getStringList("data." + worldName + "." + locStr));
                    } catch (NumberFormatException e) {
                        getLogger().severe("Could not load the location: '" + worldName + "', '" + locStr + "'");
                        e.printStackTrace();
                    }
                }
            }
        }
        return true;
    }

    public static aArmorStandCmds getInstance() {
        return instance;
    }

    public void bungeeSend(Player p, String server) {
        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.writeUTF("Connect");
        out.writeUTF(server);
        p.sendPluginMessage(this, "BungeeCord", out.toByteArray());
    }
}
