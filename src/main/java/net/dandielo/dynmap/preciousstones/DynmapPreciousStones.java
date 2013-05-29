package net.dandielo.dynmap.preciousstones;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.dynmap.DynmapAPI;
import org.dynmap.markers.MarkerAPI;

import net.dandielo.dynmap.preciousstones.layers.Fields;
import net.dandielo.dynmap.preciousstones.managers.PlayerManager;

import java.util.logging.Level;
import java.util.logging.Logger;
import net.sacredlabyrinth.Phaed.PreciousStones.PreciousStones;

@SuppressWarnings("unused")
public class DynmapPreciousStones extends JavaPlugin {
	private static DynmapPreciousStones instance;
	private static final Logger log = Logger.getLogger("Minecraft");
	private static final String LOG_PREFIX = "[Dynmap-PreciousStones] ";

	private Plugin dynmap;
	private DynmapAPI dynmapApi;
	private MarkerAPI markerApi;
	private FileConfiguration cfg;

	private Fields fields;
	private PlayerManager playerManager;
	private PreciousStones preciousstones;

	public void onEnable() {
		instance = this;
		info("initializing");

		initDynmap();
		initPStones();
		activate();

		fields = new Fields();
	}

	public void activate() {
		if (!dynmap.isEnabled() || !preciousstones.isEnabled()) {
			return;
		}

		initApis();
		
		playerManager = new PlayerManager();

		/* Load configuration */
		cfg = getConfig();
		cfg.options().copyDefaults(true); /* Load defaults, if needed */
		this.saveConfig(); /* Save updates, if needed */

		if ( cfg == null )
			this.saveDefaultConfig();
		
		getServer().getPluginManager().registerEvents(new DynmapPreciousStonesListener(), instance);
		
		// set up layers
		info("version " + this.getDescription().getVersion() + " is activated");
	}

	public void onDisable() {
	}

	private void initDynmap() {
		dynmap = getServer().getPluginManager().getPlugin("dynmap");

		if (dynmap == null) {
			severe("Cannot find dynmap!");
			return;
		}
		dynmapApi = (DynmapAPI) dynmap;
	}

	private void initPStones() {
		Plugin p = getServer().getPluginManager().getPlugin("PreciousStones");

		if (p == null) {
			severe("Cannot find PreciousStones!");
			return;
		}
		preciousstones = (PreciousStones) p;
	}

	private void initApis() {
		markerApi = dynmapApi.getMarkerAPI();

		if (markerApi == null) {
			severe("Error loading Dynmap marker API!");
			return;
		}

	}

	public static void info(String msg) {
		log.log(Level.INFO, LOG_PREFIX + msg);
	}

	public static void severe(String msg) {
		log.log(Level.SEVERE, LOG_PREFIX + msg);
	}

	public static DynmapPreciousStones getInstance() {
		return instance;
	}

	public MarkerAPI getMarkerApi() {
		return markerApi;
	}

	public DynmapAPI getDynmapApi() {
		return dynmapApi;
	}

	public FileConfiguration getCfg() {
		return cfg;
	}

	public PreciousStones getPreciousStones() {
		return preciousstones;
	}

	public PlayerManager getPlayerManager()
	{
		return playerManager;
	}
	
}

