package dev.enco.greatessentialsgui.menus;

import com.earth2me.essentials.Essentials;
import com.earth2me.essentials.Warps;
import com.earth2me.essentials.commands.WarpNotFoundException;
import dev.enco.greatessentialsgui.Main;
import dev.enco.greatessentialsgui.actions.ActionExecutor;
import dev.enco.greatessentialsgui.builder.DefaultGuiBuilder;
import dev.enco.greatessentialsgui.objects.MenuContext;
import dev.enco.greatessentialsgui.objects.MenuItem;
import dev.enco.greatessentialsgui.utils.Config;
import dev.enco.greatessentialsgui.utils.Placeholders;
import dev.enco.greatessentialsgui.utils.logger.Logger;
import dev.triumphteam.gui.builder.item.ItemBuilder;
import dev.triumphteam.gui.guis.PaginatedGui;
import lombok.Setter;
import net.ess3.api.InvalidWorldException;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class WarpsMenu {
    private final Config config;
    private final Essentials essentials = Main.getInstance().getEss();
    @Setter
    private MenuContext warpsGui;

    public WarpsMenu(Config config) {
        this.config = config;
        this.warpsGui = config.getWarpsGui();
    }

    public PaginatedGui get(Player player) throws WarpNotFoundException, InvalidWorldException {
        var gui = DefaultGuiBuilder.buildDefault(warpsGui, player, "");
        gui.setId("homes");
        setWarps(gui, player);
        DefaultGuiBuilder.updateTitle(warpsGui, gui, "");
        return gui;
    }

    private void setWarps(PaginatedGui gui, Player player) {
        var warpMenuItem = warpsGui.extraItem();
        var warps = essentials.getWarps();
        var blacklist = config.getBlacklistWarps();
        var translations = config.getWorldsTranslations();

        int[] index = {0};

        warps.getList().stream()
                .filter(key -> !blacklist.contains(key))
                .forEach(key -> {
                    index[0]++;

                    try {
                        var location = warps.getWarp(key);
                        var item = new ItemStack(warpMenuItem.itemStack());
                        var meta = item.getItemMeta();

                        String owner = getWarpOwner(key, warps);
                        var worldName = location.getWorld().getName();
                        var world = translations.getOrDefault(worldName, worldName);

                        String[] replacement = {String.valueOf(index[0]), key, world, owner, Placeholders.format(location.getX()),
                                Placeholders.format(location.getY()), Placeholders.format(location.getZ())};
                        meta.setDisplayName(Placeholders.replaceInMessage(player, meta.getDisplayName(), replacement));
                        meta.setLore(meta.getLore().stream()
                                .map(str -> Placeholders.replaceInMessage(player, str, replacement))
                                .toList()
                        );
                        item.setItemMeta(meta);

                        var guiItem = ItemBuilder.from(item).asGuiItem(e -> {
                            if (e.isLeftClick()) {
                                ActionExecutor.execute(player, gui, warpMenuItem.leftClickActions(), key);
                            } else if (e.isRightClick()) {
                                ActionExecutor.execute(player, gui, warpMenuItem.rightClickActions(), key);
                            }
                        });
                        gui.addItem(guiItem);
                    } catch (WarpNotFoundException | InvalidWorldException e) {
                        Logger.warn("An error occurred while gets warp location " + e);
                    }
                });
        if (index[0] == 0) {
            MenuItem emptyItem = warpsGui.emptyItem();
            var guiItem = ItemBuilder.from(emptyItem.itemStack()).asGuiItem(e -> {
                if (e.isLeftClick()) {
                    ActionExecutor.execute(player, gui, emptyItem.leftClickActions());
                } else if (e.isRightClick()) {
                    ActionExecutor.execute(player, gui, emptyItem.rightClickActions());
                }
            });
            for (var slot : emptyItem.slots()) gui.setItem(slot, guiItem);
        }
    }

    private String getWarpOwner(String key, Warps warps) {
        try {
            return Bukkit.getOfflinePlayer(warps.getLastOwner(key)).getName();
        } catch (Exception e) {
            Logger.warn("Warning! Your warp " + key + " was created by a non-existent player.");
            Logger.warn("This usually happens after a server wipe when you clean the map with player UUIDs.");
            Logger.warn("Please reassign this warp to an existing account.");
            return config.getIncorrectUUID();
        }
    }
}
