package com.hoopono.temporahcf;

import org.bukkit.Bukkit;

public class SeasonManager {

    private final TemporaHCF plugin;
    private boolean running = false;
    private boolean resetMarked = false;

    public SeasonManager(TemporaHCF plugin) {
        this.plugin = plugin;
    }

    public void startSeason() {
        if (running) return;
        running = true;
        Bukkit.broadcastMessage("§aTemporada iniciada! Registro cerrado.");
    }

    public void stopSeason() {
        if (!running) return;
        running = false;
        Bukkit.broadcastMessage("§cTemporada detenida.");
    }

    public void markReset() {
        resetMarked = true;
        plugin.getLogger().info("Reset marcado por admin.");
    }

    public void forceAirdrop() {
        Bukkit.broadcastMessage("§6Airdrop forzado por staff.");
    }

    public String getTopText() {
        return "Top de temporada (pendiente de implementación)";
    }

    public boolean isRunning() {
        return running;
    }

    public boolean isResetMarked() {
        return resetMarked;
    }
}
