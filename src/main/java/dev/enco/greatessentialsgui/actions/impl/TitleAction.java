package dev.enco.greatessentialsgui.actions.impl;

import dev.enco.greatessentialsgui.actions.Action;
import dev.triumphteam.gui.guis.PaginatedGui;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class TitleAction implements Action {
    @Override
    public void execute(@NotNull Player player, PaginatedGui gui, String context) {
        var args = context.split(";");
        var title = args.length > 0 ? args[0] : "";
        var subTitle = args.length > 1 ? args[1] : "";
        player.sendTitle(title, subTitle, 10, 70, 20);
    }
}
