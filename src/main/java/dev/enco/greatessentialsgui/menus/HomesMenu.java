package dev.enco.greatessentialsgui.menus;

import com.earth2me.essentials.Essentials;
import com.earth2me.essentials.User;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.LoadingCache;
import dev.enco.greatessentialsgui.Main;
import dev.enco.greatessentialsgui.builder.DefaultGuiBuilder;
import dev.enco.greatessentialsgui.objects.MenuContext;
import dev.enco.greatessentialsgui.objects.MenuItem;
import dev.enco.greatessentialsgui.utils.Config;
import dev.enco.greatessentialsgui.utils.Placeholders;
import dev.triumphteam.gui.builder.item.ItemBuilder;
import dev.triumphteam.gui.guis.PaginatedGui;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class HomesMenu {
    private final Config config;
    private final Essentials essentials = Main.getInstance().getEss();
    @Setter
    private MenuContext homesGui;

    public HomesMenu(Config config) {
        this.config = config;
        this.homesGui = config.getHomesGui();
    }

    private final LoadingCache<UUID, PaginatedGui> cachedGuis = Caffeine.newBuilder()
            .expireAfterAccess(5L, TimeUnit.MINUTES)
            .maximumSize(Bukkit.getMaxPlayers())
            .build((uuid) -> create(Bukkit.getPlayer(uuid)));

    public PaginatedGui get(UUID uuid) {
        return cachedGuis.get(uuid);
    }

    public PaginatedGui create(Player player) {
        var user = essentials.getUser(player);
        var gui = DefaultGuiBuilder.buildDefault(homesGui, "");
        gui.setId("homes");
        setHomes(gui, user, player);
        DefaultGuiBuilder.updateTitle(homesGui, gui, "");
        return gui;
    }

    public void update(Player player) {
        PaginatedGui gui = cachedGuis.getIfPresent(player.getUniqueId());
        if (gui == null) return;
        cachedGuis.invalidate(player.getUniqueId());
        boolean open = gui.getInventory().getViewers().contains(player);
        if (open) player.closeInventory();
        if (open) get(player.getUniqueId()).open(player);
    }

    private void setHomes(PaginatedGui gui, User user, Player player) {
        var homeMenuItem = homesGui.extraItem();
        var homes = user.getHomes();
        if (homes.isEmpty()) {
            MenuItem emptyItem = homesGui.emptyItem();
            var guiItem = ItemBuilder.from(emptyItem.itemStack()).asGuiItem(e -> {
                if (e.isLeftClick()) {
                    emptyItem.leftClickActions().execute(player, gui);
                } else if (e.isRightClick()) {
                    emptyItem.rightClickActions().execute(player, gui);
                }
            });
            for (var slot : emptyItem.slots()) gui.setItem(slot, guiItem);
            return;
        }
        for (int i = 0; i < homes.size(); i++) {
            var key = homes.get(i);
            var location = user.getHome(key);
            int num = i + 1;
            var item = new ItemStack(homeMenuItem.itemStack());
            var meta = item.getItemMeta();
            var translations = config.getWorldsTranslations();
            var worldName = location.getWorld().getName();
            var world = translations.getOrDefault(worldName, worldName);
            String[] replacement = {String.valueOf(num), key,
                    Placeholders.format(location.getX()),
                    Placeholders.format(location.getY()),
                    Placeholders.format(location.getZ()),
                    world,
            };
            meta.setDisplayName(Placeholders.replaceInMessage(player, meta.getDisplayName(), replacement));
            List<String> newLore = new ArrayList<>();
            for (var s : meta.getLore())
                newLore.add(Placeholders.replaceInMessage(player, s, replacement));
            meta.setLore(newLore);
            item.setItemMeta(meta);
            var guiItem = ItemBuilder.from(item).asGuiItem(e -> {
                if (e.isLeftClick()) {
                    homeMenuItem.leftClickActions().execute(player, gui, key);
                } else if (e.isRightClick()) {
                    homeMenuItem.rightClickActions().execute(player, gui, key);
                }
            });
            gui.addItem(guiItem);
        }
    }
}
