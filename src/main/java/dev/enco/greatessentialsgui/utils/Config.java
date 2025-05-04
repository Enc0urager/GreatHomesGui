package dev.enco.greatessentialsgui.utils;

import dev.enco.greatessentialsgui.Main;
import dev.enco.greatessentialsgui.actions.ActionRegistry;
import dev.enco.greatessentialsgui.actions.ActionType;
import dev.enco.greatessentialsgui.builder.ItemBuilder;
import dev.enco.greatessentialsgui.builder.SlotsParser;
import dev.enco.greatessentialsgui.objects.MenuContext;
import dev.enco.greatessentialsgui.objects.MenuItem;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import java.util.*;

@RequiredArgsConstructor @Getter
public class Config {
    public static String decimalFormat;
    private List<String> homesOpenCommands, warpsOpenCommands, blacklistWarps, previewOpenCommands;
    private MenuContext homesGui, warpsGui, kitPreviewGui;
    private final Main plugin;
    private FileConfiguration config;
    private Map<String, String> worldsTranslations;
    private Map<ActionType, List<String>> kitNotAvailableActions;
    private String noPermsMessage, priority, incorrectUUID;

    public void load() {
        long start = System.currentTimeMillis();
        this.config = plugin.getConfig();
        if (!config.contains("no-perms")) {
            config.set("no-perms", "&cИзвините, но у вас недостаточно прав");
            plugin.saveConfig();
        }
        this.kitNotAvailableActions = ActionRegistry.transform(config.getStringList("actions-on-kit-not-available"));
        this.incorrectUUID = Colorizer.colorize(config.getString("incorrect-uuid"));
        this.noPermsMessage = Colorizer.colorize(config.getString("no-perms"));
        this.priority = config.getString("event-priority");
        this.decimalFormat = config.getString("decimal-format");
        this.worldsTranslations = new HashMap<>();
        this.homesOpenCommands = config.getStringList("homes-open-commands");
        this.warpsOpenCommands = config.getStringList("warps-open-commands");
        this.previewOpenCommands = config.getStringList("preview-open-commands");
        this.blacklistWarps = config.getStringList("black-list-warps");
        var worldsSection = config.getConfigurationSection("worlds-translations");
        worldsSection.getKeys(false).forEach(world -> {
            var translation = Colorizer.colorize(worldsSection.getString(world));
            this.worldsTranslations.put(world, translation);
        });
        loadHomesGui();
        loadWarpsGui();
        loadKitPreviewGui();
        Logger.info("Конфиг успешно загружен за " + (System.currentTimeMillis() - start) + " ms.");
    }

    private void loadHomesGui() {
        var section = config.getConfigurationSection("homes-menu");
        this.homesGui = buildMenuContext(section, "home-item");
    }

    private void loadWarpsGui() {
        var section = config.getConfigurationSection("warps-menu");
        this.warpsGui = buildMenuContext(section, "warp-item");
    }

    private void loadKitPreviewGui() {
        var section = config.getConfigurationSection("kitpreview-menu");
        this.kitPreviewGui = buildMenuContext(section, null);
    }

    private MenuContext buildMenuContext(ConfigurationSection section, String extraItemPath) {
        MenuItem menuItem = null;
        if (extraItemPath != null) {
            var extraItemSection = section.getConfigurationSection(extraItemPath);
            menuItem = new MenuItem(
                    ItemBuilder.build(extraItemSection),
                    Collections.emptyList(),
                    ActionRegistry.transform(extraItemSection.getStringList("on-left-click")),
                    ActionRegistry.transform(extraItemSection.getStringList("on-right-click"))
            );
        }
        var itemsSection = section.getConfigurationSection("items");
        return new MenuContext(
                Colorizer.colorize(section.getString("title")),
                SlotsParser.parse(section.getString("border")),
                section.getInt("rows"),
                section.getInt("max-page-items"),
                menuItem,
                buildMenuItems(itemsSection)
        );

    }

    private Set<MenuItem> buildMenuItems(ConfigurationSection itemsSection) {
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
        return menuItems;
    }
}
