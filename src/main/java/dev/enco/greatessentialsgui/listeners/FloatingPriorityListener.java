package dev.enco.greatessentialsgui.listeners;

import com.earth2me.essentials.commands.WarpNotFoundException;
import dev.enco.greatessentialsgui.Main;
import dev.enco.greatessentialsgui.menus.HomesMenu;
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
        public void onCommand(PlayerCommandPreprocessEvent e) {
            this.listenCommand(e);
        }
    }),
    LOW(new FloatingPriorityListener.CommandListener() {
        @EventHandler(
                priority = EventPriority.LOW
        )
        public void onCommand(PlayerCommandPreprocessEvent e) {
            this.listenCommand(e);
        }
    }),
    NORMAL(new FloatingPriorityListener.CommandListener() {
        @EventHandler(
                priority = EventPriority.NORMAL
        )
        public void onCommand(PlayerCommandPreprocessEvent e) {
            this.listenCommand(e);
        }
    }),
    HIGH(new FloatingPriorityListener.CommandListener() {
        @EventHandler(
                priority = EventPriority.HIGH
        )
        public void onCommand(PlayerCommandPreprocessEvent e) {
            this.listenCommand(e);
        }
    }),
    HIGHEST(new FloatingPriorityListener.CommandListener() {
        @EventHandler(
                priority = EventPriority.HIGHEST
        )
        public void onCommand(PlayerCommandPreprocessEvent e) {
            this.listenCommand(e);
        }
    }),
    MONITOR(new FloatingPriorityListener.CommandListener() {
        @EventHandler(
                priority = EventPriority.MONITOR
        )
        public void onCommand(PlayerCommandPreprocessEvent e) {
            this.listenCommand(e);
        }
    });

    private final Listener listener;

    FloatingPriorityListener(Listener listener) {
        this.listener = listener;
    }

    private abstract static class CommandListener implements Listener {
        private final HomesMenu homesMenu = Main.getInstance().getHomesMenu();
        private final WarpsMenu warpsMenu = Main.getInstance().getWarpsMenu();
        private final Config config = Main.getInstance().getPluginConfig();

        public void listenCommand(PlayerCommandPreprocessEvent e) {
            Player player = e.getPlayer();
            var command = e.getMessage();
            var homeCmds = config.getHomesOpenCommands();
            var warpCmds = config.getWarpsOpenCommands();
            if (homeCmds.contains(command)) {
                e.setCancelled(true);
                homesMenu.get(player).open(player);
            } else if (warpCmds.contains(command)) {
                e.setCancelled(true);
                try {
                    warpsMenu.get(player).open(player);
                } catch (WarpNotFoundException ex) {
                    throw new RuntimeException(ex);
                } catch (InvalidWorldException ex) {
                    throw new RuntimeException(ex);
                }
            }
        }
    }
}
