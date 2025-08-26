package dev.enco.greatessentialsgui.builder;

import dev.enco.greatessentialsgui.utils.EnumUtils;
import dev.enco.greatessentialsgui.utils.logger.Logger;
import lombok.experimental.UtilityClass;
import org.bukkit.inventory.ItemFlag;

import java.util.EnumSet;
import java.util.List;

@UtilityClass
public class FlagsParser {
    public EnumSet<ItemFlag> parse(List<String> settings) {
        return EnumUtils.toEnumSet(settings,
                ItemFlag.class,
                (flag) -> Logger.warn("ItemFlag " + flag + " is not available"));
    }
}
