package com.hoopono.temporahcf;

import org.bukkit.plugin.java.JavaPlugin;

public class TemporaHCF extends JavaPlugin {

    private static TemporaHCF instance;
    private FactionManager factionManager;
    private SeasonManager seasonManager;
    private CommandTemporada commandTemporada;

    @Override
    public void onEnable() {
        instance = this;
        saveDefaultConfig();
        saveResource("messages.yml", false);

        this.factionManager = new FactionManager(this);
        this.seasonManager = new SeasonManager(this);
        this.commandTemporada = new CommandTemporada(this);

        getCommand("temporada").setExecutor(commandTemporada);
        getCommand("top").setExecutor(commandTemporada);

        getServer().getPluginManager().registerEvents(new GUIListener(this), this);

        getLogger().info("TemporaHCF habilitado");
    }

    @Override
    public void onDisable() {
        factionManager.save();
        getLogger().info("TemporaHCF deshabilitado");
    }

    public static TemporaHCF getInstance() {
        return instance;
    }

    public FactionManager getFactionManager() {
        return factionManager;
    }

    public SeasonManager getSeasonManager() {
        return seasonManager;
    }
}
