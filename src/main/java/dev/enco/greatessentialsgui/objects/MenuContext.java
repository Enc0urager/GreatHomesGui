package dev.enco.greatessentialsgui.objects;

import com.google.common.collect.ImmutableSet;
import it.unimi.dsi.fastutil.ints.IntSet;

public record MenuContext(
        String title,
        IntSet border,
        int rows,
        int maxPageItems,
        MenuItem extraItem,
        ImmutableSet<MenuItem> menuItems
) {}
