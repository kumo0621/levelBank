package com.github.kumo0621.levelbank;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public final class levelBank extends JavaPlugin implements org.bukkit.event.Listener {

    @Override
    public void onEnable() {
        // Plugin startup logic
        saveDefaultConfig();
        config = getConfig();
        getServer().getPluginManager().registerEvents(this, this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    FileConfiguration config;

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (command.getName().equals("up")) {
            if (sender instanceof Player) {
                if (args.length == 0) {
                    sender.sendMessage("/up 預けたいレベル");
                } else {
                    if (args[0].matches("[+-]?\\d*(\\.\\d+)?")) {
                        int level = Integer.parseInt(args[0]);
                        UUID uuid = ((Player) sender).getUniqueId();
                        int getLevel = Integer.parseInt(config.getString(String.valueOf(uuid), String.valueOf(level)));
                        String result = String.valueOf(getLevel += level);
                        config.set(String.valueOf(uuid), result);
                        int get = ((Player) sender).getLevel();
                        ((Player) sender).setLevel(get -= level);
                        ((Player) sender).updateInventory();
                        saveConfig();
                        sender.sendMessage(level + "レベルを預けました。");
                    } else {
                        sender.sendMessage("レベルの値がおかしいです。");
                    }
                }
            }
        }
        if (command.getName().equals("down")) {
            if (sender instanceof Player) {
                if (args.length == 0) {
                    sender.sendMessage("/down 下ろしたいレベル");
                } else {
                    if (args[0].matches("[+-]?\\d*(\\.\\d+)?")) {
                        int level = Integer.parseInt(args[0]);
                        UUID uuid = ((Player) sender).getUniqueId();
                        int getLevel = Integer.parseInt(config.getString(String.valueOf(uuid), String.valueOf(level)));
                        if(level > getLevel) {
                          sender.sendMessage("預けているレベルを引き出しレベルが超えています。");
                        }else {
                            String result = String.valueOf(getLevel -= level);
                            config.set(String.valueOf(uuid), result);
                            int get = ((Player) sender).getLevel();
                            ((Player) sender).setLevel(get += level);
                            ((Player) sender).updateInventory();
                            saveConfig();
                            sender.sendMessage(level + "レベルを引き出しました。");
                        }
                    }
                }
            }
        }
        if (command.getName().equals("bank")) {
            if (sender instanceof Player) {
                UUID uuid = ((Player) sender).getUniqueId();
                String getLevel = config.getString(String.valueOf(uuid));
                sender.sendMessage("現在「" + getLevel + "」レベル預けています。");

            }
        }
        return super.onCommand(sender, command, label, args);
    }

}
