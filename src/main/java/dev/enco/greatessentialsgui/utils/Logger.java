package dev.enco.greatessentialsgui.utils;

import lombok.experimental.UtilityClass;
import org.bukkit.Bukkit;

@UtilityClass
public class Logger {
    private java.util.logging.Logger logger = Bukkit.getLogger();

    public void warn(String message) {
        logger.warning("§7(§eGreatHomesGui§7) §6WARN §e" + message);
    }

    public void info(String message) {
        logger.info("§7(§aGreatHomesGui§7) §aINFO §f" + message);
    }

    public void error(String message) {
        logger.warning("§7(§cGreatHomesGui§7) §4ERROR §c" + message);
    }
}
