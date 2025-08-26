package dev.enco.greatessentialsgui.actions;

import dev.enco.greatessentialsgui.actions.context.Context;
import dev.triumphteam.gui.guis.PaginatedGui;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


public interface Action<C extends Context> {
    void execute(@NotNull Player player, @Nullable PaginatedGui gui, @Nullable C context, @Nullable String... replacement);
}
