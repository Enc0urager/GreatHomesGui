package dev.enco.greatessentialsgui.actions.impl;

import com.earth2me.essentials.commands.WarpNotFoundException;
import dev.enco.greatessentialsgui.Main;
import dev.enco.greatessentialsgui.actions.Action;
import dev.triumphteam.gui.guis.PaginatedGui;
import net.ess3.api.InvalidWorldException;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class ReopenAction implements Action {
    private final Main plugin = Main.getInstance();
    @Override
    public void execute(@NotNull Player player, PaginatedGui gui, String context) throws WarpNotFoundException, InvalidWorldException {
        String id = gui.getId();
        switch (id) {
            case "warps": {
                plugin.getWarpsMenu().get(player).open(player);
                break;
            }
            case "homes": {
                plugin.getHomesMenu().get(player).open(player);
                break;
            }
            default: {
                break;
            }
        }
    }
}
