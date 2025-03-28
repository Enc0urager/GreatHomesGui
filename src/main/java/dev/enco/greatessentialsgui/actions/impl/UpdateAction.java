package dev.enco.greatessentialsgui.actions.impl;

import dev.enco.greatessentialsgui.actions.Action;
import dev.triumphteam.gui.guis.PaginatedGui;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class UpdateAction implements Action {
    @Override
    public void execute(@NotNull Player player, PaginatedGui gui, String context) {
        gui.update();
    }
}
