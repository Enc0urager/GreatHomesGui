package dev.enco.greatessentialsgui.listeners;

import dev.enco.greatessentialsgui.Main;
import dev.enco.greatessentialsgui.menus.HomesMenu;
import lombok.RequiredArgsConstructor;
import net.essentialsx.api.v2.events.HomeModifyEvent;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

@RequiredArgsConstructor
public class EssentialsListener implements Listener {
    private final HomesMenu homesMenu;

    @EventHandler
    public void onHome(HomeModifyEvent e) {
        Bukkit.getScheduler().runTask(Main.getInstance(), () -> homesMenu.update(e.getHomeOwner().getBase()));
    }
}
