package dev.enco.greatessentialsgui.actions.context;

import dev.enco.greatessentialsgui.utils.logger.Logger;
import org.bukkit.Sound;

public record SoundContext(
        Sound sound,
        float volume,
        float pitch
) implements Context {
    public static SoundContext validate(String s) {
        var args = s.split(";");
        Sound sound = null;
        try {
            if (args.length >= 1) {
                sound = Sound.valueOf(args[0].toUpperCase());
            } else {
                Logger.warn("Sound can't be null, context " + s);
                return null;
            }
        } catch (IllegalArgumentException e) {
            Logger.warn("Sound " + args[0] + "doesn't exist, context " + s);
        }
        try {
            float volume = args.length > 1 ? Float.parseFloat(args[1]) : 1;
            float pitch = args.length > 2 ? Float.parseFloat(args[2]) : 1;
            return new SoundContext(sound, volume, pitch);
        } catch (NumberFormatException e) {
            Logger.warn("Volume and pitch must be a number, context " + s);
        }
        return null;
    }
}
