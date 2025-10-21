package dev.enco.greatessentialsgui.utils;

import dev.enco.greatessentialsgui.Main;
import dev.enco.greatessentialsgui.actions.ActionFactory;
import dev.enco.greatessentialsgui.actions.ActionMap;
import dev.enco.greatessentialsgui.builder.ItemBuilder;
import dev.enco.greatessentialsgui.builder.SlotsParser;
import dev.enco.greatessentialsgui.commands.EmptyCommand;
import dev.enco.greatessentialsgui.commands.KitPreviewEmptyCommand;
import dev.enco.greatessentialsgui.objects.MenuContext;
import dev.enco.greatessentialsgui.objects.MenuItem;
import dev.enco.greatessentialsgui.utils.colorizer.Colorizer;
import dev.enco.greatessentialsgui.utils.logger.Logger;
import it.unimi.dsi.fastutil.objects.Object2ObjectMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import it.unimi.dsi.fastutil.objects.ObjectSet;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.command.Command;
import org.bukkit.command.CommandMap;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

import java.lang.reflect.Field;
import java.util.Map;

@RequiredArgsConstructor @Getter
public class Config {
    public static String decimalFormat;
    private ObjectSet<String> homesOpenCommands, warpsOpenCommands, blacklistWarps, previewOpenCommands;
    private MenuContext homesGui, warpsGui, kitPreviewGui;
    private final Main plugin;
    private FileConfiguration config;
    private final Object2ObjectMap<String, String> worldsTranslations = new Object2ObjectOpenHashMap<>(),
            kitNames = new Object2ObjectOpenHashMap<>();
    private ActionMap kitNotAvailableActions;
    private String noPermsMessage, priority, incorrectUUID;
    @Getter
    private static boolean usingPapi;

    public void load() {
        long start = System.currentTimeMillis();
        this.config = plugin.getConfig();
        if (!config.contains("no-perms")) {
            config.set("no-perms", "&cИзвините, но у вас недостаточно прав");
            plugin.saveConfig();
        }
        if (!config.contains("kit-names")) {
            config.set("kit-names", Map.of("start", "Игрок", "vip", "Вип"));
            plugin.saveConfig();
        }
        ConfigurationSection section = config.getConfigurationSection("kit-names");
        section.getKeys(false).forEach(key -> kitNames.put(key, section.getString(key)));
        Colorizer.setColorizer(config.getString("colorizer"));
        this.usingPapi = config.getBoolean("use-papi");
        this.kitNotAvailableActions = ActionFactory.from(config.getStringList("actions-on-kit-not-available"));
        this.incorrectUUID = Colorizer.colorize(config.getString("incorrect-uuid"));
        this.noPermsMessage = Colorizer.colorize(config.getString("no-perms"));
        this.priority = config.getString("event-priority");
        this.decimalFormat = config.getString("decimal-format");
        CommandMap map = getCommandMap();
        this.homesOpenCommands = new ObjectOpenHashSet<>(config.getStringList("homes-open-commands"));
        this.warpsOpenCommands = new ObjectOpenHashSet<>(config.getStringList("warps-open-commands"));
        this.previewOpenCommands = new ObjectOpenHashSet<>(config.getStringList("preview-open-commands"));
        registerCommands(map, homesOpenCommands, false);
        registerCommands(map, warpsOpenCommands, false);
        registerCommands(map, previewOpenCommands, true);
        this.blacklistWarps = new ObjectOpenHashSet<>(config.getStringList("black-list-warps"));
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

    public String getKitName(String kit) {
        return kitNames.getOrDefault(kit, kit);
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
        MenuItem emptyItem = null;
        if (extraItemPath != null) {
            menuItem = buildItem(section.getConfigurationSection(extraItemPath));
            emptyItem = buildItem(section.getConfigurationSection("empty-item"));
        }
        var itemsSection = section.getConfigurationSection("items");
        return new MenuContext(
                Colorizer.colorize(section.getString("title")),
                SlotsParser.parse(section.getString("border")),
                section.getInt("rows"),
                section.getInt("max-page-items"),
                menuItem,
                emptyItem,
                buildMenuItems(itemsSection)
        );

    }

    private ObjectSet<MenuItem> buildMenuItems(ConfigurationSection itemsSection) {
        ObjectSet<MenuItem> menuItems = new ObjectOpenHashSet<>();
        if (itemsSection != null && !itemsSection.getKeys(false).isEmpty()) {
            itemsSection.getKeys(false).forEach(item -> {
                var itemSection = itemsSection.getConfigurationSection(item);
                menuItems.add(buildItem(itemSection));
            });
        }
        return menuItems;
    }

    private MenuItem buildItem(ConfigurationSection itemSection) {
        return new MenuItem(
                ItemBuilder.build(itemSection),
                SlotsParser.parse(itemSection.getString("slots")),
                ActionFactory.from(itemSection.getStringList("on-left-click")),
                ActionFactory.from(itemSection.getStringList("on-right-click"))
        );
    }

    private CommandMap getCommandMap() {
        try {
            Server server = Bukkit.getServer();
            Field commandMapField = server.getClass().getDeclaredField("commandMap");
            commandMapField.setAccessible(true);
            return (CommandMap) commandMapField.get(server);
        } catch (Exception e) {
            Logger.error("An error occured while getting CommandMap " + e);
        }
        return null;
    }

    private void registerCommands(CommandMap commandMap, ObjectSet<String> commands, boolean kitPreview) {
        for (String command : commands) {
            String clean = command.substring(1);
            if (isRegistered(commandMap, clean)) continue;

            Command cmd = kitPreview ? new KitPreviewEmptyCommand(clean) : new EmptyCommand(clean);
            commandMap.register("gegui", cmd);
            Logger.info("Команда " + clean + " успешно зарегистрирована");
        }
    }

    private boolean isRegistered(CommandMap map, String command) {
        Command cmd = map.getCommand(command);
        return cmd != null && cmd.isRegistered();
    }
}
