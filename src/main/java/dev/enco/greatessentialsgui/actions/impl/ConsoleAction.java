package dev.enco.greatessentialsgui.actions.impl;

import dev.enco.greatessentialsgui.actions.Action;
import dev.enco.greatessentialsgui.actions.context.StringContext;
import dev.enco.greatessentialsgui.utils.Placeholders;
import dev.triumphteam.gui.guis.PaginatedGui;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ConsoleAction implements Action<StringContext> {
    @Override
    public void execute(@NotNull Player player, @Nullable PaginatedGui gui, @Nullable StringContext context, @Nullable String... replacement) {
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), Placeholders.replaceInMessage(player, context.string(), replacement));
    }
}
