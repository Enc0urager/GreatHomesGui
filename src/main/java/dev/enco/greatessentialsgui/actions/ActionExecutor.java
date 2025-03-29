package dev.enco.greatessentialsgui.actions;

import com.earth2me.essentials.commands.WarpNotFoundException;
import dev.triumphteam.gui.guis.PaginatedGui;
import lombok.experimental.UtilityClass;
import net.ess3.api.InvalidWorldException;
import org.bukkit.entity.Player;
import java.util.List;
import java.util.Map;

@UtilityClass
public class ActionExecutor {
    public void execute(Player player, PaginatedGui gui, Map<ActionType, List<String>> actions, String name) {
        actions.keySet().forEach(type -> {
            var contexts = actions.get(type);
            for (String context : contexts) {
                try {
                    type.getAction().execute(player, gui, context
                            .replace("{name}", name).replace("{player}", player.getName()));
                } catch (WarpNotFoundException e) {
                    throw new RuntimeException(e);
                } catch (InvalidWorldException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }
}
