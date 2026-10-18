package com.hoopono.temporahcf;

import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public class GUIListener implements Listener {

    private final TemporaHCF plugin;

    public GUIListener(TemporaHCF plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent e) {
        if (e.getInventory() == null || e.getCurrentItem() == null) return;
        if (!e.getInventory().getTitle().equals("TemporaHCF")) return;

        e.setCancelled(true);

        ItemStack it = e.getCurrentItem();
        if (!it.hasItemMeta()) return;
        String name = it.getItemMeta().getDisplayName();

        if (name.contains("Entrar")) {
            e.getWhoClicked().closeInventory();
            e.getWhoClicked().sendMessage(ChatColor.GREEN + "Usa /temporada entrar");
        } else if (name.contains("Tienda")) {
            e.getWhoClicked().closeInventory();
            e.getWhoClicked().sendMessage(ChatColor.GOLD + "Tienda en construcción.");
        } else if (name.contains("Top")) {
            e.getWhoClicked().closeInventory();
            e.getWhoClicked().sendMessage(ChatColor.AQUA + plugin.getSeasonManager().getTopText());
        }
    }
}
