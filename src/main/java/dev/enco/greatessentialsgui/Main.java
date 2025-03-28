package dev.enco.greatessentialsgui;

import com.earth2me.essentials.Essentials;
import dev.enco.greatessentialsgui.listeners.FloatingPriorityListener;
import dev.enco.greatessentialsgui.menus.HomesMenu;
import dev.enco.greatessentialsgui.menus.WarpsMenu;
import dev.enco.greatessentialsgui.utils.Config;
import dev.enco.greatessentialsgui.utils.Logger;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

@Getter
public final class Main extends JavaPlugin {
    @Getter
    private static Main instance;
    private final Essentials ess = (Essentials) Bukkit.getServer().getPluginManager().getPlugin("Essentials");
    private HomesMenu homesMenu;
    private WarpsMenu warpsMenu;
    private Config pluginConfig;

    @Override
    public void onEnable() {
        instance = this;
        this.pluginConfig = new Config(this);
        pluginConfig.load();
        this.homesMenu = new HomesMenu(pluginConfig);
        this.warpsMenu = new WarpsMenu(pluginConfig);
        FloatingPriorityListener listener;
        try {
            listener = FloatingPriorityListener.valueOf(this.pluginConfig.priority);
        } catch (IllegalArgumentException var8) {
           Logger.warn("Несуществующий приоритет слушателя, используем HIGHEST");
            listener = FloatingPriorityListener.HIGHEST;
        }
        this.getServer().getPluginManager().registerEvents(listener.getListener(), this);
        Logger.info("Плагин GreatEssentialsGui успешно влючён");
        Logger.info("Автор - Encourager, версия - " + this.getDescription().getVersion());
    }

    @Override
    public void onDisable() {
        Logger.info("Плагин GreatEssentialsGui успешно выключен");
        Logger.info("Автор - Encourager, версия - " + this.getDescription().getVersion());
    }
}
