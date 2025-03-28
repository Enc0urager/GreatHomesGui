package dev.enco.greatessentialsgui.actions.impl;

import dev.enco.greatessentialsgui.actions.Action;
import dev.triumphteam.gui.guis.PaginatedGui;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class PrevPageAction implements Action {
    @Override
    public void execute(@NotNull Player player, PaginatedGui gui, String context) {
        gui.previous();
        gui.updateTitle(gui.getTitle().replace("{current_page}", String.valueOf(gui.getCurrentPageNum())).replace("{pages}", String.valueOf(gui.getPagesNum())));
    }
}
