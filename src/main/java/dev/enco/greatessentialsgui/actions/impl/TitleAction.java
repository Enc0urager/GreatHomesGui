package dev.enco.greatessentialsgui.actions.impl;

import dev.enco.greatessentialsgui.actions.Action;
import dev.enco.greatessentialsgui.actions.context.TitleContext;
import dev.enco.greatessentialsgui.utils.Placeholders;
import dev.triumphteam.gui.guis.PaginatedGui;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class TitleAction implements Action<TitleContext> {

    @Override
    public void execute(@NotNull Player player, @Nullable PaginatedGui gui, @Nullable TitleContext context, @Nullable String... replacement) {
        player.sendTitle(Placeholders.replaceInMessage(player, context.title(), replacement),
                Placeholders.replaceInMessage(player, context.subtitle(), replacement),
                context.fadeIn(), context.fadeIn(), context.fadeOut());
    }
}
