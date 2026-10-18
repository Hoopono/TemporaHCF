package com.hoopono.temporahcf;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class FactionManager {

    private final TemporaHCF plugin;
    private final File file;
    private final YamlConfiguration cfg;

    private final Map<String, List<UUID>> registered = new LinkedHashMap<String, List<UUID>>();

    public FactionManager(TemporaHCF plugin) {
        this.plugin = plugin;
        this.file = new File(plugin.getDataFolder(), "factions.yml");
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        this.cfg = YamlConfiguration.loadConfiguration(file);
        load();
    }

    public void toggleRegister(Player p) {
        String existing = getFactionOf(p);
        if (existing != null) {
            removePlayerFromFaction(existing, p.getUniqueId());
            p.sendMessage("Has salido de la facción " + existing);
            save();
            return;
        }

        String name = p.getName();
        if (!registered.containsKey(name)) {
            registered.put(name, new ArrayList<UUID>());
        }

        List<UUID> members = registered.get(name);
        if (members.size() >= plugin.getConfig().getInt("players-per-faction", 3)) {
            p.sendMessage("La facción ya tiene el máximo de jugadores.");
            return;
        }

        members.add(p.getUniqueId());
        p.sendMessage("Facción creada/actualizada: " + name);
        save();
    }

    public void removeFromRegistration(Player p) {
        String f = getFactionOf(p);
        if (f == null) {
            p.sendMessage("No estás registrado en ninguna facción.");
            return;
        }
        removePlayerFromFaction(f, p.getUniqueId());
        p.sendMessage("Has salido de la facción " + f);
        save();
    }

    public void removePlayerFromFaction(String faction, UUID uid) {
        List<UUID> list = registered.get(faction);
        if (list != null) {
            list.remove(uid);
            if (list.isEmpty()) {
                registered.remove(faction);
            }
        }
    }

    public String getFactionOf(Player p) {
        for (Map.Entry<String, List<UUID>> e : registered.entrySet()) {
            for (UUID u : e.getValue()) {
                if (u.equals(p.getUniqueId())) {
                    return e.getKey();
                }
            }
        }
        return null;
    }

    public String listRegistered() {
        if (registered.isEmpty()) {
            return "No hay facciones registradas.";
        }
        StringBuilder sb = new StringBuilder("Facciones registradas:\n");
        for (Map.Entry<String, List<UUID>> e : registered.entrySet()) {
            sb.append("- ").append(e.getKey()).append(" (").append(e.getValue().size()).append(")\n");
        }
        return sb.toString();
    }

    public void save() {
        cfg.set("factions", null);
        for (Map.Entry<String, List<UUID>> e : registered.entrySet()) {
            List<String> ids = new ArrayList<String>();
            for (UUID u : e.getValue()) {
                ids.add(u.toString());
            }
            cfg.set("factions." + e.getKey() + ".members", ids);
        }
        try {
            cfg.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void load() {
        if (cfg.contains("factions")) {
            for (String key : cfg.getConfigurationSection("factions").getKeys(false)) {
                List<String> ids = cfg.getStringList("factions." + key + ".members");
                List<UUID> uuids = new ArrayList<UUID>();
                for (String s : ids) {
                    try {
                        uuids.add(UUID.fromString(s));
                    } catch (IllegalArgumentException ignored) {
                    }
                }
                registered.put(key, uuids);
            }
        }
    }
}
