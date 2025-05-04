package dev.enco.greatessentialsgui.menus;

import com.earth2me.essentials.Essentials;
import com.earth2me.essentials.User;
import dev.enco.greatessentialsgui.Main;
import dev.enco.greatessentialsgui.actions.ActionExecutor;
import dev.enco.greatessentialsgui.builder.DefaultGuiBuilder;
import dev.enco.greatessentialsgui.objects.MenuContext;
import dev.enco.greatessentialsgui.utils.Config;
import dev.enco.greatessentialsgui.utils.Number;
import dev.triumphteam.gui.builder.item.ItemBuilder;
import dev.triumphteam.gui.guis.PaginatedGui;
import lombok.Setter;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import java.util.ArrayList;
import java.util.List;

public class HomesMenu {
    private final Config config;
    private final Essentials essentials = Main.getInstance().getEss();
    @Setter
    private MenuContext homesGui;

    public HomesMenu(Config config) {
        this.config = config;
        this.homesGui = config.getHomesGui();
    }

    public PaginatedGui get(Player player) {
        var user = essentials.getUser(player);
        var gui = DefaultGuiBuilder.buildDefault(homesGui, player);
        gui.setId("homes");
        setHomes(gui, user, player);
        DefaultGuiBuilder.updateTitle(homesGui, gui);
        return gui;
    }

    private void setHomes(PaginatedGui gui, User user, Player player) {
        var homeMenuItem = homesGui.extraItem();
        var homes = user.getHomes();
        for (int i = 0; i < homes.size(); i++) {
            var key = homes.get(i);
            var location = user.getHome(key);
            int num = i + 1;
            var item = new ItemStack(homeMenuItem.itemStack());
            var meta = item.getItemMeta();
            List<String> replacedLore = new ArrayList<>();
            var translations = config.getWorldsTranslations();
            var worldName = location.getWorld().getName();
            var world = translations.getOrDefault(worldName, worldName);
            meta.getLore().forEach(str -> {
                replacedLore.add(str.replace("{num}", String.valueOf(num))
                        .replace("{name}", key)
                        .replace("{world}", world)
                        .replace("{x}", Number.format(location.getX()))
                        .replace("{y}", Number.format(location.getY()))
                        .replace("{z}", Number.format(location.getZ())));
            });
            meta.setDisplayName(meta.getDisplayName().replace("{num}", String.valueOf(num)).replace("{name}", key));
            meta.setLore(replacedLore);
            item.setItemMeta(meta);
            var guiItem = ItemBuilder.from(item).asGuiItem(e -> {
                if (e.isLeftClick()) {
                    ActionExecutor.execute(player, gui, homeMenuItem.leftClickActions(), key);
                } else if (e.isRightClick()) {
                    ActionExecutor.execute(player, gui, homeMenuItem.rightClickActions(), key);
                }
            });
            gui.addItem(guiItem);
        }
    }
}
