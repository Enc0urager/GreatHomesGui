package dev.enco.greatessentialsgui;

import com.earth2me.essentials.Essentials;
import dev.enco.greatessentialsgui.commands.ReloadCommand;
import dev.enco.greatessentialsgui.listeners.EssentialsListener;
import dev.enco.greatessentialsgui.listeners.FloatingPriorityListener;
import dev.enco.greatessentialsgui.menus.HomesMenu;
import dev.enco.greatessentialsgui.menus.KitPreviewMenu;
import dev.enco.greatessentialsgui.menus.WarpsMenu;
import dev.enco.greatessentialsgui.utils.Config;
import dev.enco.greatessentialsgui.utils.logger.Logger;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.lang.reflect.InvocationTargetException;

@Getter
public final class Main extends JavaPlugin {
    @Getter
    private static Main instance;
    private final Essentials ess = (Essentials) Bukkit.getServer().getPluginManager().getPlugin("Essentials");
    private Config pluginConfig;
    @Setter
    private HomesMenu homesMenu;
    private WarpsMenu warpsMenu;
    private KitPreviewMenu kitPreviewMenu;

    @Override
    public void onEnable() {
        instance = this;
        Logger.setup();
        saveDefaultConfig();
        this.pluginConfig = new Config(this);
        pluginConfig.load();
        this.homesMenu = new HomesMenu(pluginConfig);
        this.warpsMenu = new WarpsMenu(pluginConfig);
        try {
            this.kitPreviewMenu = new KitPreviewMenu(pluginConfig);
        } catch (InvocationTargetException | IllegalAccessException | NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
        FloatingPriorityListener listener;
        try {
            listener = FloatingPriorityListener.valueOf(this.pluginConfig.getPriority());
        } catch (IllegalArgumentException e) {
            Logger.warn("Неизвестный приоритет слушателя, используем HIGHEST");
            listener = FloatingPriorityListener.HIGHEST;
        }
        var pm = this.getServer().getPluginManager();
        pm.registerEvents(listener.getListener(), this);
        pm.registerEvents(new EssentialsListener(homesMenu, warpsMenu), this);
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
