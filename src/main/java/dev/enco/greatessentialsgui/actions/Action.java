package dev.enco.greatessentialsgui.actions;

import com.earth2me.essentials.commands.WarpNotFoundException;
import dev.triumphteam.gui.guis.PaginatedGui;
import net.ess3.api.InvalidWorldException;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;


public interface Action {
    void execute(@NotNull Player player, PaginatedGui gui, String context) throws Exception;
}
