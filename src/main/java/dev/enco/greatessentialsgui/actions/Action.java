package dev.enco.greatessentialsgui.actions;

import dev.triumphteam.gui.guis.PaginatedGui;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;


public interface Action {
    void execute(@NotNull Player player, PaginatedGui gui, String context);
}
