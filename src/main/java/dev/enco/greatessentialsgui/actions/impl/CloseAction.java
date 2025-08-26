package dev.enco.greatessentialsgui.actions.impl;

import dev.enco.greatessentialsgui.actions.Action;
import dev.enco.greatessentialsgui.actions.context.GuiContext;
import dev.triumphteam.gui.guis.PaginatedGui;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class CloseAction implements Action<GuiContext> {

    @Override
    public void execute(@NotNull Player player, @Nullable PaginatedGui gui, @Nullable GuiContext context, @Nullable String... replacement) {
        gui.close(player);
    }
}
