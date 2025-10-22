package dev.enco.greatessentialsgui.actions.impl;

import com.earth2me.essentials.commands.WarpNotFoundException;
import dev.enco.greatessentialsgui.Main;
import dev.enco.greatessentialsgui.actions.Action;
import dev.enco.greatessentialsgui.actions.context.GuiContext;
import dev.triumphteam.gui.guis.PaginatedGui;
import net.ess3.api.InvalidWorldException;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ReopenAction implements Action<GuiContext> {
    private final Main plugin = Main.getInstance();

    @Override
    public void execute(@NotNull Player player, @Nullable PaginatedGui gui, @Nullable GuiContext context, @Nullable String... replacement) {
        String id = gui.getId();
        switch (id) {
            case "warps": {
                try {
                    plugin.getWarpsMenu().get(player).open(player);
                } catch (WarpNotFoundException | InvalidWorldException ignored) {}
                break;
            }
            case "homes": {
                plugin.getHomesMenu().get(player.getUniqueId()).open(player);
                break;
            }
            default: {
                break;
            }
        }
    }
}
