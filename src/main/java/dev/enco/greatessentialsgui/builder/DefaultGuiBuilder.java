package dev.enco.greatessentialsgui.builder;

import dev.enco.greatessentialsgui.actions.ActionExecutor;
import dev.enco.greatessentialsgui.objects.MenuContext;
import dev.triumphteam.gui.builder.item.ItemBuilder;
import dev.triumphteam.gui.guis.Gui;
import dev.triumphteam.gui.guis.PaginatedGui;
import lombok.experimental.UtilityClass;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.entity.Player;

@UtilityClass
public class DefaultGuiBuilder {
    public PaginatedGui buildDefault(MenuContext context, Player player) {
        var gui = Gui.paginated().title(Component.text(context.title()))
                .rows(context.rows()).pageSize(context.maxPageItems()).disableAllInteractions().create();
        if (context.menuItems() != null && !context.menuItems().isEmpty()) {
            context.menuItems().forEach(menuItem -> {
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
        context.border().forEach(slot -> {
            gui.setItem(slot, ItemBuilder.from(Material.AIR).asGuiItem());
        });
        return gui;
    }

    public void updateTitle(MenuContext context, PaginatedGui gui) {
        gui.updateTitle(context.title().
                replace("{current_page}", String.valueOf(gui.getCurrentPageNum()))
                .replace("{pages}", String.valueOf(gui.getPagesNum())));
        gui.update();
    }
}
