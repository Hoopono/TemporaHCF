package com.hoopono.temporahcf;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CommandTemporada implements CommandExecutor, TabCompleter {

    private final TemporaHCF plugin;

    public CommandTemporada(TemporaHCF plugin) {
        this.plugin = plugin;
    }

    private Inventory createMainMenu(Player p) {
        Inventory inv = Bukkit.createInventory(null, 9, "TemporaHCF");

        ItemStack register = new ItemStack(Material.FEATHER);
        ItemMeta m = register.getItemMeta();
        m.setDisplayName(ChatColor.GREEN + "Entrar/Salir registro");
        m.setLore(Collections.singletonList(ChatColor.GRAY + "Usa /temporada entrar"));
        register.setItemMeta(m);

        ItemStack shop = new ItemStack(Material.CHEST);
        ItemMeta ms = shop.getItemMeta();
        ms.setDisplayName(ChatColor.GOLD + "Tienda");
        ms.setLore(Collections.singletonList(ChatColor.GRAY + "Abrir /shop"));
        shop.setItemMeta(ms);

        ItemStack top = new ItemStack(Material.PAPER);
        ItemMeta mt = top.getItemMeta();
        mt.setDisplayName(ChatColor.AQUA + "Top de facciones");
        mt.setLore(Collections.singletonList(ChatColor.GRAY + "Ver ranking"));
        top.setItemMeta(mt);

        inv.setItem(1, register);
        inv.setItem(4, shop);
        inv.setItem(7, top);
        return inv;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Este comando solo puede usarse desde el juego.");
            return true;
        }

        Player p = (Player) sender;

        if (args.length == 0) {
            p.openInventory(createMainMenu(p));
            return true;
        }

        String sub = args[0].toLowerCase();

        switch (sub) {
            case "entrar":
                plugin.getFactionManager().toggleRegister(p);
                return true;
            case "salir":
                plugin.getFactionManager().removeFromRegistration(p);
                return true;
            case "list":
                p.sendMessage(plugin.getFactionManager().listRegistered());
                return true;
            case "top":
                p.sendMessage(plugin.getSeasonManager().getTopText());
                return true;
            case "shop":
                p.sendMessage("Tienda en construcción.");
                return true;
            case "admin":
                if (!p.hasPermission("tempora.admin")) {
                    p.sendMessage(ChatColor.RED + "No tienes permiso.");
                    return true;
                }
                if (args.length < 2) {
                    p.sendMessage(ChatColor.YELLOW + "/temporada admin <start|stop|reset|airdrop>");
                    return true;
                }
                handleAdmin(p, args[1]);
                return true;
            default:
                p.sendMessage(ChatColor.RED + "Subcomando desconocido.");
                return true;
        }
    }

    private void handleAdmin(Player p, String action) {
        switch (action.toLowerCase()) {
            case "start":
                plugin.getSeasonManager().startSeason();
                p.sendMessage(ChatColor.GREEN + "Temporada iniciada.");
                break;
            case "stop":
                plugin.getSeasonManager().stopSeason();
                p.sendMessage(ChatColor.GREEN + "Temporada detenida.");
                break;
            case "reset":
                plugin.getSeasonManager().markReset();
                p.sendMessage(ChatColor.GREEN + "Reset marcado.");
                break;
            case "airdrop":
                plugin.getSeasonManager().forceAirdrop();
                p.sendMessage(ChatColor.GREEN + "Airdrop forzado.");
                break;
            default:
                p.sendMessage(ChatColor.RED + "Acción admin desconocida.");
                break;
        }
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (args.length == 1) {
            List<String> list = new ArrayList<String>();
            list.add("entrar");
            list.add("salir");
            list.add("list");
            list.add("top");
            list.add("shop");
            list.add("admin");
            return list;
        }
        if (args.length == 2 && args[0].equalsIgnoreCase("admin")) {
            List<String> list = new ArrayList<String>();
            list.add("start");
            list.add("stop");
            list.add("reset");
            list.add("airdrop");
            return list;
        }
        return new ArrayList<String>();
    }
}
