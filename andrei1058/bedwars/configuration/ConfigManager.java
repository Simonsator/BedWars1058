package com.andrei1058.bedwars.configuration;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import static com.andrei1058.bedwars.Main.plugin;

public class ConfigManager {

    private YamlConfiguration yml;
    private File config;
    private String name;

    public ConfigManager(String name, String dir, boolean arena){
        File d = new File(dir);
        if (!d.exists()){
            d.mkdir();
        }
        config = new File(dir+"/"+name+".yml");
        if (!config.exists()){
            try {
                config.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
            plugin.getLogger().info("Creating "+dir+"/"+name+".yml");
        }
        yml = YamlConfiguration.loadConfiguration(config);
        if (arena){
            yml.addDefault("group", "DEFAULT");
            yml.addDefault("minPlayers", 2);
            yml.addDefault("maxInTeam", 1);
            yml.addDefault("allowSpectate", true);
            yml.addDefault("spawnProtection", 5);
            yml.addDefault("shopProtection", 1);
            yml.addDefault("upgradesProtection", 1);
            yml.addDefault("islandRadius", 10);
            yml.addDefault("bedBlock", "BED_BLOCK");
            yml.addDefault("worldBorder", 300);
            yml.addDefault("voidKill", false);
            yml.options().copyDefaults(true);
            save();
        }
        this.name = name;
    }

    public String stringLocationArenaFormat(Location loc){
        return Double.valueOf(loc.getX()) + "," + Double.valueOf(loc.getY()) + "," + Double.valueOf(loc.getZ() )+ "," + Double.valueOf(loc.getYaw()) + "," + Double.valueOf(loc.getPitch());
    }

    public String stringLocationConfigFormat(Location loc){
        return Double.valueOf(loc.getX()) + "," + Double.valueOf(loc.getY()) + "," + Double.valueOf(loc.getZ() )+ "," + Double.valueOf(loc.getYaw()) + "," + Double.valueOf(loc.getPitch())+","+loc.getWorld().getName();
    }

    public void saveConfigLoc(String path, Location loc){
        String data = Double.valueOf(loc.getX()) + "," + Double.valueOf(loc.getY()) + "," + Double.valueOf(loc.getZ() )+ "," + Double.valueOf(loc.getYaw()) + "," + Double.valueOf(loc.getPitch())+","+loc.getWorld().getName();
        yml.set(path, data);
        save();
    }

    public void saveArenaLoc(String path, Location loc){
        String data = Double.valueOf(loc.getX()) + "," + Double.valueOf(loc.getY()) + "," + Double.valueOf(loc.getZ() )+ "," + Double.valueOf(loc.getYaw()) + "," + Double.valueOf(loc.getPitch());
        yml.set(path, data);
        save();
    }

    public String getConfigLoc(Location loc){
        return loc.getX() + "," + loc.getY() + "," + loc.getZ() + "," + loc.getYaw() + "," + loc.getPitch() + "," + loc.getWorld().getName();
    }

    public Location getConfigLoc(String path){
        String d = yml.getString(path);
        String[] data = d.replace("[", "").replace("]", "").split(",");
        return new Location(Bukkit.getWorld(data[5]), Double.valueOf(data[0]), Double.valueOf(data[1]), Double.valueOf(data[2]), Float.valueOf(data[3]), Float.valueOf(data[4]));
    }

    public Location getArenaLoc(String path){
        String d = yml.getString(path);
        String[] data = d.replace("[", "").replace("]", "").split(",");
        return new Location(Bukkit.getWorld(name), Double.valueOf(data[0]), Double.valueOf(data[1]), Double.valueOf(data[2]), Float.valueOf(data[3]), Float.valueOf(data[4]));
    }
    public Location fromArenaStringList(String string){
        String[] data = string.split(",");
        return new Location(Bukkit.getWorld(name), Double.valueOf(data[0]), Double.valueOf(data[1]), Double.valueOf(data[2]), Float.valueOf(data[3]), Float.valueOf(data[4]));

    }

    public void set(String path, Object value){
        yml.set(path, value);
        save();
    }

    public YamlConfiguration getYml() {
        return yml;
    }

    public void save(){
        try {
            yml.save(config);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getLobbyWorldName(){
        if (yml.get("lobbyLoc") == null) return "";
        String d = yml.getString("lobbyLoc");
        String[] data = d.replace("[", "").replace("]", "").split(",");
        return data[data.length-1];
    }

    public List<String> l(String path){
        return yml.getStringList(path).stream().map(s -> s.replace("&", "§")).collect(Collectors.toList());
    }

    public boolean getBoolean(String path){
        return yml.getBoolean(path);
    }

    public int getInt(String path){
        return yml.getInt(path);
    }
}
