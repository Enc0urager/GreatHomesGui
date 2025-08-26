package dev.enco.greatessentialsgui.builder;

import dev.enco.greatessentialsgui.utils.logger.Logger;
import lombok.experimental.UtilityClass;
import org.bukkit.enchantments.Enchantment;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@UtilityClass
public class EnchantsParser {
    public Map<Enchantment, Integer> parse(List<String> settings) {
        Map<Enchantment, Integer> map = new HashMap<>();
        for (var s : settings) {
            var enchStr = s.split(";");
            Enchantment enchantment = Enchantment.MENDING;
            int level = 1;
            try {
                enchantment = Enchantment.getByName(enchStr[0]);
            } catch (Exception e) {
                Logger.warn("Зачарование " + enchStr[0] + " не найдено, используем MENDING");
            }
            try {
                level = Integer.parseInt(enchStr[1]);
            } catch (NumberFormatException e) {
                Logger.warn("Уровень зачарования должен быть числом, используем 1");
            }
            map.put(enchantment, level);
        }
        return map;
    }
}
