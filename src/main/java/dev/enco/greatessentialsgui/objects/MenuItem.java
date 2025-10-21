package dev.enco.greatessentialsgui.objects;

import dev.enco.greatessentialsgui.actions.ActionMap;
import org.bukkit.inventory.ItemStack;

public record MenuItem (
        ItemStack itemStack,
        int[] slots,
        ActionMap leftClickActions,
        ActionMap rightClickActions
) {}
