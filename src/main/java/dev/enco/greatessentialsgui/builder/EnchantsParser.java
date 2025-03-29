package dev.enco.greatessentialsgui.builder;

import dev.enco.greatessentialsgui.utils.ImmutablePair;
import dev.enco.greatessentialsgui.utils.Logger;
import lombok.experimental.UtilityClass;
import org.bukkit.enchantments.Enchantment;

import java.util.ArrayList;
import java.util.List;

@UtilityClass
public class EnchantsParser {
    public List<ImmutablePair<Enchantment, Integer>> parse(List<String> settings) {
        List<ImmutablePair<Enchantment, Integer>> enchants = new ArrayList<>();
        settings.forEach(s -> {
            var enchStr = s.split(";");
            Enchantment enchantment;
            int level;
            try {
                enchantment = Enchantment.getByName(enchStr[0]);
            } catch (Exception e) {
                Logger.warn("Зачарование " + enchStr[0] + " не найдено, используем MENDING");
                enchantment = Enchantment.MENDING;
            }
            try {
                level = Integer.parseInt(enchStr[1]);
            } catch (NumberFormatException e) {
                Logger.warn("Уровень зачарования должен быть числом, используем 1");
                level = 1;
            }
            var enchant = ImmutablePair.of(enchantment, level);
            enchants.add(enchant);
        });
        return enchants;
    }
}
