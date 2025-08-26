package dev.enco.greatessentialsgui.objects;

import com.google.common.collect.ImmutableMap;
import dev.enco.greatessentialsgui.actions.ActionType;
import dev.enco.greatessentialsgui.actions.context.Context;
import it.unimi.dsi.fastutil.ints.IntSet;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public record MenuItem (
        ItemStack itemStack,
        IntSet slots,
        ImmutableMap<ActionType, List<Context>> leftClickActions,
        ImmutableMap<ActionType, List<Context>> rightClickActions
) {}
