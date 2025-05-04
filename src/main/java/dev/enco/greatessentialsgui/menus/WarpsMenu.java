package dev.enco.greatessentialsgui.menus;

import com.earth2me.essentials.Essentials;
import com.earth2me.essentials.commands.WarpNotFoundException;
import dev.enco.greatessentialsgui.Main;
import dev.enco.greatessentialsgui.actions.ActionExecutor;
import dev.enco.greatessentialsgui.builder.DefaultGuiBuilder;
import dev.enco.greatessentialsgui.objects.MenuContext;
import dev.enco.greatessentialsgui.utils.Config;
import dev.enco.greatessentialsgui.utils.Number;
import dev.triumphteam.gui.builder.item.ItemBuilder;
import dev.triumphteam.gui.guis.PaginatedGui;
import lombok.Setter;
import net.ess3.api.InvalidWorldException;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

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
        var gui = DefaultGuiBuilder.buildDefault(warpsGui, player);
        gui.setId("homes");
        setWarps(gui, player);
        DefaultGuiBuilder.updateTitle(warpsGui, gui);
        return gui;
    }

    private void setWarps(PaginatedGui gui, Player player) throws WarpNotFoundException, InvalidWorldException {
        var warpMenuItem = warpsGui.extraItem();
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
            var offlinePlayer = Bukkit.getOfflinePlayer(warps.getLastOwner(key));
            String owner;
            if (offlinePlayer != null) owner = offlinePlayer.getName();
            else owner = config.getIncorrectUUID();
            var worldName = location.getWorld().getName();
            var world = translations.getOrDefault(worldName, worldName);
            var x = Number.format(location.getX());
            var y = Number.format(location.getY());
            var z = Number.format(location.getZ());
            int finalDeleted = deleted;
            int finalNum = num - finalDeleted;
            meta.getLore().forEach(str -> {
                replacedLore.add(str.replace("{num}", String.valueOf(finalNum))
                        .replace("{name}", key)
                        .replace("{world}", world)
                        .replace("{owner}", owner)
                        .replace("{x}", x)
                        .replace("{y}", y)
                        .replace("{z}", z));
            });
            meta.setDisplayName(meta.getDisplayName().replace("{num}", String.valueOf(finalNum)).replace("{name}", key));
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
