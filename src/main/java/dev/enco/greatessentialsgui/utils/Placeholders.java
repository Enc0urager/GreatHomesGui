package dev.enco.greatessentialsgui.utils;

import dev.enco.greatessentialsgui.utils.colorizer.Colorizer;
import lombok.experimental.UtilityClass;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.entity.Player;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.MessageFormat;
import java.util.Locale;

@UtilityClass
public class Placeholders {
    private static final DecimalFormatSymbols symbols = new DecimalFormatSymbols(Locale.US);
    private static final DecimalFormat df = new DecimalFormat("#.#", symbols);

    public String replaceInMessage(Player player, String s, String... replacement) {
        return replace(player, MessageFormat.format(s, replacement));
    }

    public String replace(Player player, String s) {
        if (Config.isUsingPapi() && PlaceholderAPI.containsPlaceholders(s)) {
            return Colorizer.colorize(PlaceholderAPI.setPlaceholders(player, s));
        }
        return s;
    }

    public String format(double value) {
        return df.format(value);
    }
}
