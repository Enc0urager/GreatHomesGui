package dev.enco.greatessentialsgui.objects;

import dev.enco.greatessentialsgui.actions.ActionType;
import org.bukkit.inventory.ItemStack;
import java.util.List;
import java.util.Map;

public record MenuItem (
        ItemStack itemStack,
        List<Integer> slots,
        Map<ActionType, List<String>> leftClickActions,
        Map<ActionType, List<String>> rightClickActions
) {}
