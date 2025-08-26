package dev.enco.greatessentialsgui.actions;

import com.google.common.collect.ImmutableMap;
import dev.enco.greatessentialsgui.actions.context.Context;
import dev.triumphteam.gui.guis.PaginatedGui;
import lombok.experimental.UtilityClass;
import org.bukkit.entity.Player;

import java.util.List;

@UtilityClass
public class ActionExecutor {
    public void execute(Player player, PaginatedGui gui, ImmutableMap<ActionType, List<Context>> actions, String... replacement) {
        for (var type : actions.keySet()) {
            var contexts = actions.get(type);
            var action = type.getAction();
            for (Context context : contexts)
                action.execute(player, gui, context, replacement);
        }
    }
}
