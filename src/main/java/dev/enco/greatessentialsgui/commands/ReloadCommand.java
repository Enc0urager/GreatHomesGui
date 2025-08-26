package dev.enco.greatessentialsgui.commands;

import dev.enco.greatessentialsgui.Main;
import dev.enco.greatessentialsgui.utils.Config;
import lombok.RequiredArgsConstructor;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

@RequiredArgsConstructor
public class ReloadCommand implements CommandExecutor, TabCompleter {
    private final Main plugin;
    private final Config config;

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String s, @NotNull String[] args) {
        if (!sender.hasPermission("greatessentialsgui.admin")) {
            sender.sendMessage(config.getNoPermsMessage());
            return true;
        }
        var start = System.currentTimeMillis();
        plugin.reloadConfig();
        config.load();
        plugin.getHomesMenu().setHomesGui(config.getHomesGui());
        plugin.getWarpsMenu().setWarpsGui(config.getWarpsGui());
        plugin.getKitPreviewMenu().setContext(config.getKitPreviewGui());
        sender.sendMessage(ChatColor.GREEN + "Конфиг перезагружен за " + (System.currentTimeMillis() - start) + " ms.");
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String s, @NotNull String[] args) {
        if (args.length == 1) {
            return List.of("reload");
        }
        return null;
    }
}
