package net.dandielo.dynmap.preciousstones;

import java.util.List;

import net.sacredlabyrinth.Phaed.PreciousStones.FieldFlag;
import net.sacredlabyrinth.Phaed.PreciousStones.managers.EntryManager;
import net.sacredlabyrinth.Phaed.PreciousStones.vectors.Field;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;

public class DynmapPreciousStonesListener implements Listener {

    DynmapPreciousStones plugin;
    
    public DynmapPreciousStonesListener()
    {
    	plugin = DynmapPreciousStones.getInstance();
    }
    
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e)
    {
		plugin.getPlayerManager().addEntry(e.getPlayer());
    }
    
	@EventHandler
	public void onPlayerMove(PlayerMoveEvent e)
	{
		EntryManager em = plugin.getPreciousStones().getEntryManager();
		
		List<Field> fields = em.getPlayerEntryFields(e.getPlayer());
		if ( fields == null )
		{
			plugin.getDynmapApi().assertPlayerVisibility(e.getPlayer(), true, plugin);
			plugin.getPlayerManager().getEntry(e.getPlayer()).setVisible(true);
		}
		else
		{
			for ( Field f : fields )
			{
				if ( f.hasFlag(FieldFlag.DYNMAP_HIDE_PLAYERS) && f.isAllowed(e.getPlayer().getName()) )
				{
					plugin.getDynmapApi().assertPlayerVisibility(e.getPlayer(), false, plugin);
					plugin.getPlayerManager().getEntry(e.getPlayer()).setVisible(false);
				}
				else
				if ( f.hasFlag(FieldFlag.DYNMAP_SHOW_PLAYERS) && f.isAllowed(e.getPlayer().getName()) )
				{
					plugin.getDynmapApi().assertPlayerVisibility(e.getPlayer(), true, plugin);
					plugin.getPlayerManager().getEntry(e.getPlayer()).setVisible(true);
				}
			}
		}
					
	}
}
