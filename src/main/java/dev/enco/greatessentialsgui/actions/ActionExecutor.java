package dev.enco.greatessentialsgui.actions;

import dev.enco.greatessentialsgui.utils.Logger;
import dev.triumphteam.gui.guis.PaginatedGui;
import lombok.experimental.UtilityClass;
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
                } catch (Exception e) {
                    Logger.warn("Ошибка при выполнении действия " + context + " " + e);
                }
            }
        });
    }
}
