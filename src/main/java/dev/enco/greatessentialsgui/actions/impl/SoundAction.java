package dev.enco.greatessentialsgui.actions.impl;

import dev.enco.greatessentialsgui.actions.Action;
import dev.enco.greatessentialsgui.utils.Logger;
import dev.triumphteam.gui.guis.PaginatedGui;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class SoundAction implements Action {
    @Override
    public void execute(@NotNull Player player, PaginatedGui gui, String context) {
        var args = context.split(";");
        Sound sound = null;
        try {
            if (args.length >= 1) {
                sound = Sound.valueOf(args[0].toUpperCase());
            } else {
                Logger.warn("Отсутствует звук проверьте конфиг");
            }
        } catch (IllegalArgumentException e) {
            Logger.warn("Звук " + args[0] + " не существует, проверьте конфиг!");
        }

        try {
            float volume = args.length > 1 ? Float.parseFloat(args[1]) : 1;
            float pitch = args.length > 2 ? Float.parseFloat(args[2]) : 1;
            player.playSound(player.getLocation(), sound, volume, pitch);
        } catch (NumberFormatException e) {
            Logger.warn("Громкость и тон звука должны быть числом! Проверьте конфиг!");
        }
    }
}
