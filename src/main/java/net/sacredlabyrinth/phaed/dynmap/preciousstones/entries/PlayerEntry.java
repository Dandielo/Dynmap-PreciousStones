package net.sacredlabyrinth.phaed.dynmap.preciousstones.entries;

import net.sacredlabyrinth.phaed.dynmap.preciousstones.DynmapPreciousStones;

import org.bukkit.entity.Player;

/**
 * Borrowed from phaeds DynmapSimpleClans, Thanks! :) 
 * @author Phaed240
 *
 */	
public class PlayerEntry {
	private String name;
    private boolean visible;

    public PlayerEntry(Player player)
    {
        this.name = player.getName();

        // start player not visible

        setVisible(false);
    }


    public Player getPlayer()
    {
        return DynmapPreciousStones.getInstance().getServer().getPlayer(name);
    }

    public boolean isVisible()
    {
        return visible;
    }

    public void setVisible(boolean visible)
    {
        Player player = getPlayer();

        if (player != null)
        {
            this.visible = visible;
            DynmapPreciousStones.getInstance().getDynmapApi().setPlayerVisiblity(player, visible);
        }
    }

    public String getName()
    {
        return name;
    }

    @Override
    public int hashCode()
    {
        return name.hashCode() >> 13;
    }

    @Override
    public boolean equals(Object obj)
    {
        if (!(obj instanceof PlayerEntry))
        {
            return false;
        }

        PlayerEntry other = (PlayerEntry) obj;
        return other.getName().equals(this.getName());
    }

    @Override
    public String toString()
    {
        return name;
    }
}
