package dev.enco.greatessentialsgui.utils;

import dev.enco.greatessentialsgui.Main;
import dev.enco.greatessentialsgui.actions.ActionRegistry;
import dev.enco.greatessentialsgui.builder.ItemBuilder;
import dev.enco.greatessentialsgui.builder.SlotsParser;
import dev.enco.greatessentialsgui.objects.HomesGui;
import dev.enco.greatessentialsgui.objects.MenuItem;
import dev.enco.greatessentialsgui.objects.WarpsGui;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.*;

@RequiredArgsConstructor @Getter
public class Config {
    public String priority;
    public static String decimalFormat;
    private List<String> homesOpenCommands;
    private List<String> warpsOpenCommands;
    private List<String> blacklistWarps;
    private HomesGui homesGui;
    private WarpsGui warpsGui;
    private final Main plugin;
    private FileConfiguration config;
    private Map<String, String> worldsTranslations;

    public void load() {
        long start = System.currentTimeMillis();
        plugin.saveDefaultConfig();
        config = plugin.getConfig();
        priority = config.getString("event-priority");
        decimalFormat = config.getString("decimal-format");
        worldsTranslations = new HashMap<>();
        this.homesOpenCommands = config.getStringList("homes-open-commands");
        this.warpsOpenCommands = config.getStringList("warps-open-commands");
        this.blacklistWarps = config.getStringList("black-list-warps");
        var worldsSection = config.getConfigurationSection("worlds-translations");
        worldsSection.getKeys(false).forEach(world -> {
            var translation = Colorizer.colorize(worldsSection.getString(world));
            worldsTranslations.put(world, translation);
        });
        loadHomesGui();
        loadWarpsGui();
        Logger.info("Конфиг загружен за " + (System.currentTimeMillis() - start) + " ms.");
    }

    private void loadHomesGui() {
        var section = config.getConfigurationSection("homes-menu");
        var homeItemSection = section.getConfigurationSection("home-item");
        var itemsSection = section.getConfigurationSection("items");
        Set<MenuItem> menuItems = new HashSet<>();
        if (itemsSection != null && !itemsSection.getKeys(false).isEmpty()) {
            itemsSection.getKeys(false).forEach(item -> {
                var itemSection = itemsSection.getConfigurationSection(item);
                var menuItem = new MenuItem(
                        ItemBuilder.build(itemSection),
                        SlotsParser.parse(itemSection.getString("slots")),
                        ActionRegistry.transform(itemSection.getStringList("on-left-click")),
                        ActionRegistry.transform(itemSection.getStringList("on-right-click"))
                );
                menuItems.add(menuItem);
            });
        }
        homesGui = new HomesGui(
                Colorizer.colorize(section.getString("title")),
                SlotsParser.parse(section.getString("border")),
                section.getInt("rows"),
                section.getInt("max-page-items"),
                new MenuItem(
                        ItemBuilder.build(homeItemSection),
                        Collections.emptyList(),
                        ActionRegistry.transform(homeItemSection.getStringList("on-left-click")),
                        ActionRegistry.transform(homeItemSection.getStringList("on-right-click"))
                ),
                menuItems
        );
    }

    private void loadWarpsGui() {
        var section = config.getConfigurationSection("warps-menu");
        var warpItemSection = section.getConfigurationSection("warp-item");
        var itemsSection = section.getConfigurationSection("items");
        Set<MenuItem> menuItems = new HashSet<>();
        if (itemsSection != null && !itemsSection.getKeys(false).isEmpty()) {
            itemsSection.getKeys(false).forEach(item -> {
                var itemSection = itemsSection.getConfigurationSection(item);
                var menuItem = new MenuItem(
                        ItemBuilder.build(itemSection),
                        SlotsParser.parse(itemSection.getString("slots")),
                        ActionRegistry.transform(itemSection.getStringList("on-left-click")),
                        ActionRegistry.transform(itemSection.getStringList("on-right-click"))
                );
                menuItems.add(menuItem);
            });
        }
        warpsGui = new WarpsGui(
                Colorizer.colorize(section.getString("title")),
                SlotsParser.parse(section.getString("border")),
                section.getInt("rows"),
                section.getInt("max-page-items"),
                new MenuItem(
                        ItemBuilder.build(warpItemSection),
                        Collections.emptyList(),
                        ActionRegistry.transform(warpItemSection.getStringList("on-left-click")),
                        ActionRegistry.transform(warpItemSection.getStringList("on-right-click"))
                ),
                menuItems
        );
    }
}
