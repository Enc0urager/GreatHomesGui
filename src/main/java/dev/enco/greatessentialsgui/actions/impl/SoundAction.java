package dev.enco.greatessentialsgui.actions.impl;

import dev.enco.greatessentialsgui.actions.Action;
import dev.enco.greatessentialsgui.actions.context.SoundContext;
import dev.triumphteam.gui.guis.PaginatedGui;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class SoundAction implements Action<SoundContext> {
    @Override
    public void execute(@NotNull Player player, @Nullable PaginatedGui gui, @Nullable SoundContext context, @Nullable String... replacement) {
        player.playSound(player.getLocation(), context.sound(), context.volume(), context.pitch());
    }
}
