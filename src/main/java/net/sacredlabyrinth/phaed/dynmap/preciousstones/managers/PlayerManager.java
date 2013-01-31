package net.sacredlabyrinth.phaed.dynmap.preciousstones.managers;

import net.sacredlabyrinth.phaed.dynmap.preciousstones.DynmapPreciousStones;
import net.sacredlabyrinth.phaed.dynmap.preciousstones.entries.PlayerEntry;

import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

public class PlayerManager
{

    DynmapPreciousStones plugin;
    private Map<String, PlayerEntry> players = new HashMap<String, PlayerEntry>();

    public PlayerManager()
    {
        plugin = DynmapPreciousStones.getInstance();
    }

    public void addEntry(Player player)
    {
        if (!players.containsKey(player.getName())) {
            players.put(player.getName(), new PlayerEntry(player));
        }
    }

    public PlayerEntry getEntry(Player player)
    {
        if (!players.containsKey(player.getName())) {
            addEntry(player);
        }

        return players.get(player.getName());
    }
    
    public PlayerEntry getEntry(String player)
    {
    	return getEntry(plugin.getServer().getPlayer(player));
    }
}