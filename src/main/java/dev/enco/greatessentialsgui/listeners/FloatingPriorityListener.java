package dev.enco.greatessentialsgui.listeners;

import com.earth2me.essentials.Essentials;
import com.earth2me.essentials.commands.WarpNotFoundException;
import dev.enco.greatessentialsgui.Main;
import dev.enco.greatessentialsgui.menus.HomesMenu;
import dev.enco.greatessentialsgui.menus.KitPreviewMenu;
import dev.enco.greatessentialsgui.menus.WarpsMenu;
import dev.enco.greatessentialsgui.utils.Config;
import lombok.Getter;
import net.ess3.api.InvalidWorldException;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

@Getter
public enum FloatingPriorityListener {
    LOWEST(new FloatingPriorityListener.CommandListener() {
        @EventHandler(
                priority = EventPriority.LOWEST
        )
        public void onCommand(PlayerCommandPreprocessEvent e) throws Exception {
            this.listenCommand(e);
        }
    }),
    LOW(new FloatingPriorityListener.CommandListener() {
        @EventHandler(
                priority = EventPriority.LOW
        )
        public void onCommand(PlayerCommandPreprocessEvent e) throws Exception {
            this.listenCommand(e);
        }
    }),
    NORMAL(new FloatingPriorityListener.CommandListener() {
        @EventHandler(
                priority = EventPriority.NORMAL
        )
        public void onCommand(PlayerCommandPreprocessEvent e) throws Exception {
            this.listenCommand(e);
        }
    }),
    HIGH(new FloatingPriorityListener.CommandListener() {
        @EventHandler(
                priority = EventPriority.HIGH
        )
        public void onCommand(PlayerCommandPreprocessEvent e) throws Exception {
            this.listenCommand(e);
        }
    }),
    HIGHEST(new FloatingPriorityListener.CommandListener() {
        @EventHandler(
                priority = EventPriority.HIGHEST
        )
        public void onCommand(PlayerCommandPreprocessEvent e) throws Exception {
            this.listenCommand(e);
        }
    }),
    MONITOR(new FloatingPriorityListener.CommandListener() {
        @EventHandler(
                priority = EventPriority.MONITOR
        )
        public void onCommand(PlayerCommandPreprocessEvent e) throws Exception {
            this.listenCommand(e);
        }
    });

    private final Listener listener;

    FloatingPriorityListener(Listener listener) {
        this.listener = listener;
    }

    private abstract static class CommandListener implements Listener {
        private final WarpsMenu warpsMenu = Main.getInstance().getWarpsMenu();
        private final KitPreviewMenu previewMenu = Main.getInstance().getKitPreviewMenu();
        private final Config config = Main.getInstance().getPluginConfig();
        private final Essentials essentials = Main.getInstance().getEss();

        public void listenCommand(PlayerCommandPreprocessEvent e) throws Exception {
            Player player = e.getPlayer();
            var command = e.getMessage();
            var homeCmds = config.getHomesOpenCommands();
            var warpCmds = config.getWarpsOpenCommands();
            var previewCmds = config.getPreviewOpenCommands();
            var parts = command.split(" ");
            if (parts.length == 2 && previewCmds.contains(parts[0])) {
                e.setCancelled(true);
                var kitName = parts[1];
                if (essentials.getKits().getKitKeys().contains(kitName))
                    previewMenu.get(kitName).open(player);
                else config.getKitNotAvailableActions().execute(player, null,  kitName);
            }
            if (homeCmds.contains(command)) {
                e.setCancelled(true);
                Main.getInstance().getHomesMenu().get(player.getUniqueId()).open(player);
            } else if (warpCmds.contains(command)) {
                e.setCancelled(true);
                warpsMenu.get().open(player);
            }
        }
    }
}
