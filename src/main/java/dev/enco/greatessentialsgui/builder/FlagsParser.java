package dev.enco.greatessentialsgui.builder;

import dev.enco.greatessentialsgui.utils.Logger;
import lombok.experimental.UtilityClass;
import org.bukkit.inventory.ItemFlag;

import java.util.ArrayList;
import java.util.List;

@UtilityClass
public class FlagsParser {
    public List<ItemFlag> parse(List<String> settings) {
        List<ItemFlag> flags = new ArrayList<>();
        settings.forEach(s -> {
            try {
                var itemFlag = ItemFlag.valueOf(s);
                flags.add(itemFlag);
            } catch (IllegalArgumentException e) {
                Logger.warn("Флаг " + s + " не найден");
            }
        });
        return flags;
    }
}
