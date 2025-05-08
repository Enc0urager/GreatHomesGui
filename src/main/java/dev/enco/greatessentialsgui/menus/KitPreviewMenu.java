package dev.enco.greatessentialsgui.menus;

import com.earth2me.essentials.Essentials;
import com.earth2me.essentials.Kit;
import com.earth2me.essentials.MetaItemStack;
import com.earth2me.essentials.libs.snakeyaml.external.biz.base64Coder.Base64Coder;
import dev.enco.greatessentialsgui.Main;
import dev.enco.greatessentialsgui.builder.DefaultGuiBuilder;
import dev.enco.greatessentialsgui.objects.MenuContext;
import dev.enco.greatessentialsgui.utils.Config;
import dev.enco.greatessentialsgui.utils.Logger;
import dev.triumphteam.gui.builder.item.ItemBuilder;
import dev.triumphteam.gui.guis.PaginatedGui;
import lombok.Setter;
import net.ess3.provider.SerializationProvider;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

public class KitPreviewMenu {
    private final Essentials essentials = Main.getInstance().getEss();
    private final SerializationProvider serializationProvider;
    @Setter
    private MenuContext context;

    public KitPreviewMenu(Config config) throws InvocationTargetException, NoSuchMethodException, IllegalAccessException {
        this.context = config.getKitPreviewGui();
        this.serializationProvider = init();
    }

    private SerializationProvider init() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        try {
            return essentials.getSerializationProvider();
        } catch (NoSuchMethodError e) {
            Logger.info("Обнаружен dev билд");
            var method = essentials.getClass().getMethod("getProviders");
            var providers = method.invoke(essentials);
            var getMethod = providers.getClass().getMethod("get", Class.class);
            return (SerializationProvider) getMethod.invoke(providers, SerializationProvider.class);
        }
    }

    public PaginatedGui get(Player player, String kitName) throws Exception {
        var gui = DefaultGuiBuilder.buildDefault(context, player);
        gui.setId("kit " + kitName);
        setKitItems(gui, kitName);
        gui.updateTitle(context.title()
                .replace("{name}", kitName)
                .replace("{current_page}", String.valueOf(gui.getCurrentPageNum()))
                .replace("{pages}", String.valueOf(gui.getPagesNum())));
        gui.update();
        return gui;
    }

    private void setKitItems(PaginatedGui gui, String kitName) throws Exception {
        for (var item : getKitItems(kitName)) gui.addItem(ItemBuilder.from(item).asGuiItem());
    }

    private List<ItemStack> getKitItems(String kitName) throws Exception {
        Kit kit = new Kit(kitName, essentials);
        List<String> kitItems = kit.getItems();
        List<ItemStack> items = new ArrayList<>();
        for (String item : kitItems) {
            final ItemStack stack;
            if (item.startsWith("@")) {
                stack = serializationProvider.deserializeItem(Base64Coder.decodeLines(item.substring(1)));
            } else {
                final String[] parts = item.split(" +");
                final ItemStack parseStack = essentials.getItemDb().get(parts[0], parts.length > 1 ? Integer.parseInt(parts[1]) : 1);
                if (parseStack.getType() == Material.AIR) {
                    continue;
                }
                final MetaItemStack metaStack = new MetaItemStack(parseStack);
                if (parts.length > 2) {
                    metaStack.parseStringMeta(null, true, parts, 2, essentials);
                }
                stack = metaStack.getItemStack();
            }
            items.add(stack);
        }
        return items;
    }
}
