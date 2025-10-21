package dev.enco.greatessentialsgui.objects;

import com.google.common.collect.ImmutableSet;

public record MenuContext(
        String title,
        int[] border,
        int rows,
        int maxPageItems,
        MenuItem extraItem,
        MenuItem emptyItem,
        ImmutableSet<MenuItem> menuItems
) {}
