package dev.enco.greatessentialsgui.actions;

import dev.enco.greatessentialsgui.actions.context.Context;
import dev.triumphteam.gui.guis.PaginatedGui;
import org.bukkit.entity.Player;

public class ActionMap {
    public static final ActionMap EMPTY = new ActionMap(new ActionType[0], new Context[0][]);
    private final ActionType[] types;
    private final Context[][] contexts;
    private final int size;

    public ActionMap(ActionType[] types, Context[][] contexts) {
        this.types = types;
        this.contexts = contexts;
        this.size = types.length;
    }

    public void execute(Player player, PaginatedGui gui, String... replacements) {
        for (int i = 0; i < size; i++) {
            ActionType type = types[i];
            Context[] typeContexts = contexts[i];
            var action = type.getAction();
            for (Context context : typeContexts)
                action.execute(player, gui, context, replacements);
        }
    }
}
