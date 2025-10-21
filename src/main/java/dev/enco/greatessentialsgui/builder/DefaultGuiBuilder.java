package dev.enco.greatessentialsgui.builder;

import dev.enco.greatessentialsgui.objects.MenuContext;
import dev.triumphteam.gui.builder.item.ItemBuilder;
import dev.triumphteam.gui.guis.Gui;
import dev.triumphteam.gui.guis.PaginatedGui;
import lombok.experimental.UtilityClass;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.text.MessageFormat;

@UtilityClass
public class DefaultGuiBuilder {
    public PaginatedGui buildDefault(MenuContext context, Player player, String... replacement) {
        var gui = Gui.paginated().title(Component.text(context.title()))
                .rows(context.rows()).pageSize(context.maxPageItems()).disableAllInteractions().create();
        if (context.menuItems() != null && !context.menuItems().isEmpty()) {
            for (var menuItem : context.menuItems()) {
                var guiItem = ItemBuilder.from(menuItem.itemStack()).asGuiItem(e -> {
                    if (e.isLeftClick()) {
                        menuItem.leftClickActions().execute(player, gui, replacement);
                    } else if (e.isRightClick()) {
                        menuItem.rightClickActions().execute(player, gui, replacement);
                    }
                });
                for (var slot : menuItem.slots()) {
                    gui.setItem(slot, guiItem);
                }
            }
        }
        for (var slot : context.border()) {
            gui.setItem(slot, ItemBuilder.from(Material.AIR).asGuiItem());
        }
        return gui;
    }

    public void updateTitle(MenuContext context, PaginatedGui gui, String kitName) {
        gui.updateTitle(MessageFormat.format(context.title(),
                String.valueOf(gui.getCurrentPageNum()), String.valueOf(gui.getPagesNum()), kitName));
        gui.update();
    }
}
