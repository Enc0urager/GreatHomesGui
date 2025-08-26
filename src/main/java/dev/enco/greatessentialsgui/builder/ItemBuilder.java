package dev.enco.greatessentialsgui.builder;

import com.destroystokyo.paper.profile.ProfileProperty;
import dev.enco.greatessentialsgui.utils.colorizer.Colorizer;
import dev.enco.greatessentialsgui.utils.logger.Logger;
import lombok.experimental.UtilityClass;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@UtilityClass
public class ItemBuilder {
    public ItemStack build(ConfigurationSection section) {
        ItemStack itemStack;
        var material = section.getString("material");
        int amount = section.getInt("amount");
        if (material.startsWith("basehead-")) {
            var head = new ItemStack(Material.PLAYER_HEAD, amount);
            var skinUrl = material.replace("basehead-", "");
            try {
                var meta = (SkullMeta) head.getItemMeta();
                var profile = Bukkit.createProfile(UUID.randomUUID());
                profile.setProperty(new ProfileProperty("textures", skinUrl));
                meta.setPlayerProfile(profile);
                head.setItemMeta(meta);
            } catch (Exception e) {
                Logger.warn("Ошибка при загрузке текстуры головы " + material + " " + e);
            }
            itemStack = head;
        } else {
            itemStack = new ItemStack(Material.valueOf(material), amount);
        }
        var meta = itemStack.getItemMeta();
        List<String> lore = new ArrayList<>();
        section.getStringList("lore").forEach(s -> {
            lore.add(Colorizer.colorize(s));
        });
        meta.setLore(lore);
        meta.setDisplayName(Colorizer.colorize(section.getString("name")));
        meta.setCustomModelData(section.getInt("model-data"));
        var flags = FlagsParser.parse(section.getStringList("item-flags"));
        for (var flag : flags) {
            meta.addItemFlags(flag);
        }
        var enchants = EnchantsParser.parse(section.getStringList("enchantments"));
        enchants.forEach((enchant, level) -> {
            meta.addEnchant(enchant, level, true);
        });
        itemStack.setItemMeta(meta);
        return itemStack;
    }
}
