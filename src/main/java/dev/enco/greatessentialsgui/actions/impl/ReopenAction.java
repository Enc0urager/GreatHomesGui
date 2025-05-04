package dev.enco.greatessentialsgui.actions.impl;

import dev.enco.greatessentialsgui.Main;
import dev.enco.greatessentialsgui.actions.Action;
import dev.triumphteam.gui.guis.PaginatedGui;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class ReopenAction implements Action {
    private final Main plugin = Main.getInstance();
    @Override
    public void execute(@NotNull Player player, PaginatedGui gui, String context) throws Exception {
        String id = gui.getId();
        if (id.startsWith("kit"))
            plugin.getKitPreviewMenu().get(player, id.split(" ")[1]);
        else {
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
}
