package dev.enco.greatessentialsgui;

import com.earth2me.essentials.Essentials;
import dev.enco.greatessentialsgui.commands.ReloadCommand;
import dev.enco.greatessentialsgui.listeners.FloatingPriorityListener;
import dev.enco.greatessentialsgui.menus.HomesMenu;
import dev.enco.greatessentialsgui.menus.WarpsMenu;
import dev.enco.greatessentialsgui.utils.Config;
import dev.enco.greatessentialsgui.utils.Logger;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

@Getter
public final class Main extends JavaPlugin {
    @Getter
    private static Main instance;
    private final Essentials ess = (Essentials) Bukkit.getServer().getPluginManager().getPlugin("Essentials");
    private Config pluginConfig;
    @Setter
    private HomesMenu homesMenu;
    private WarpsMenu warpsMenu;

    @Override
    public void onEnable() {
        instance = this;
        saveDefaultConfig();
        this.pluginConfig = new Config(this);
        pluginConfig.load();
        this.homesMenu = new HomesMenu(pluginConfig);
        this.warpsMenu = new WarpsMenu(pluginConfig);
        FloatingPriorityListener listener;
        try {
            listener = FloatingPriorityListener.valueOf(this.pluginConfig.priority);
        } catch (IllegalArgumentException e) {
            Logger.warn("Неизвестный приоритет слушателя, используем HIGHEST");
            listener = FloatingPriorityListener.HIGHEST;
        }
        this.getServer().getPluginManager().registerEvents(listener.getListener(), this);
        var reloadCommand = new ReloadCommand(this, pluginConfig);
        var command = getCommand("greatessentialsgui");
        command.setExecutor(reloadCommand);
        command.setTabCompleter(reloadCommand);
        Logger.info("Плагин GreatEssentialsGui успешно включён");
        Logger.info("Автор - Encourager, Версия - " + this.getDescription().getVersion());
    }

    @Override
    public void onDisable() {
        Logger.info("Плагин GreatEssentialsGui успешно выключен");
        Logger.info("Автор - Encourager, Версия - " + this.getDescription().getVersion());
    }
}
