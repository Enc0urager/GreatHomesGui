package dev.enco.greatessentialsgui.menus;

import com.earth2me.essentials.Essentials;
import com.earth2me.essentials.commands.WarpNotFoundException;
import dev.enco.greatessentialsgui.Main;
import dev.enco.greatessentialsgui.actions.ActionExecutor;
import dev.enco.greatessentialsgui.objects.WarpsGui;
import dev.enco.greatessentialsgui.utils.Colorizer;
import dev.enco.greatessentialsgui.utils.Config;
import dev.enco.greatessentialsgui.utils.Number;
import dev.triumphteam.gui.builder.item.ItemBuilder;
import dev.triumphteam.gui.guis.Gui;
import dev.triumphteam.gui.guis.PaginatedGui;
import net.ess3.api.InvalidWorldException;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class WarpsMenu {
    private final Config config;
    private WarpsGui warpsGui;
    private Essentials essentials = Main.getInstance().getEss();

    public WarpsMenu(Config config) {
        this.config = config;
        this.warpsGui = config.getWarpsGui();
    }

    public PaginatedGui get(Player player) throws WarpNotFoundException, InvalidWorldException {
        var gui = Gui.paginated().title(Component.text(Colorizer.colorize(warpsGui.title()))).rows(warpsGui.rows()).pageSize(warpsGui.maxPageItems()).disableAllInteractions().create();
        if (warpsGui.menuItems() != null && !warpsGui.menuItems().isEmpty()) {
            warpsGui.menuItems().forEach(menuItem -> {
                var guiItem = ItemBuilder.from(menuItem.itemStack()).asGuiItem(e -> {
                    if (e.isLeftClick()) {
                        ActionExecutor.execute(player, gui, menuItem.leftClickActions(), "");
                    } else if (e.isRightClick()) {
                        ActionExecutor.execute(player, gui, menuItem.rightClickActions(), "");
                    }
                });
                menuItem.slots().forEach(slot -> {
                    gui.setItem(slot, guiItem);
                });
            });
        }
        warpsGui.border().forEach(slot -> {
            gui.setItem(slot, ItemBuilder.from(Material.AIR).asGuiItem());
        });
        setWarps(gui, player);
        gui.updateTitle(warpsGui.title().
                replace("{current_page}", String.valueOf(gui.getCurrentPageNum()))
                .replace("{pages}", String.valueOf(gui.getPagesNum())));
        gui.update();
        return gui;
    }

    private void setWarps(PaginatedGui gui, Player player) throws WarpNotFoundException, InvalidWorldException {
        var warpMenuItem = warpsGui.warpItem();
        var warps = essentials.getWarps();
        var warpsList = new ArrayList<>(warps.getList());
        var blacklist = config.getBlacklistWarps();
        int deleted = 0;
        for (int i = 0; i < warpsList.size(); i++) {
            var key = warpsList.get(i);
            if (blacklist.contains(key)) {
                deleted++;
                continue;
            }
            var location = warps.getWarp(key);
            int num = i + 1;
            var item = new ItemStack(warpMenuItem.itemStack());
            var meta = item.getItemMeta();
            List<String> replacedLore = new ArrayList<>();
            var translations = config.getWorldsTranslations();
            var worldName = location.getWorld().getName();
            var owner = Bukkit.getOfflinePlayer(warps.getLastOwner(key)).getName();
            var world = translations.getOrDefault(worldName, "Добавьте мир в конфиг");
            var x = Number.format(location.getX());
            var y = Number.format(location.getY());
            var z = Number.format(location.getZ());
            int finalDeleted = deleted;
            meta.getLore().forEach(str -> {
                replacedLore.add(str.replace("{num}", String.valueOf(num - finalDeleted))
                        .replace("{name}", key)
                        .replace("{world}", world)
                        .replace("{owner}", owner)
                        .replace("{x}", x)
                        .replace("{y}", y)
                        .replace("{z}", z));
            });
            meta.setDisplayName(meta.getDisplayName().replace("{num}", String.valueOf(num - finalDeleted)).replace("{name}", key));
            meta.setLore(replacedLore);
            item.setItemMeta(meta);
            var guiItem = ItemBuilder.from(item).asGuiItem(e -> {
                if (e.isLeftClick()) {
                    ActionExecutor.execute(player, gui, warpMenuItem.leftClickActions(), key);
                } else if (e.isRightClick()) {
                    ActionExecutor.execute(player, gui, warpMenuItem.rightClickActions(), key);
                }
            });
            gui.addItem(guiItem);
        }
    }
}
