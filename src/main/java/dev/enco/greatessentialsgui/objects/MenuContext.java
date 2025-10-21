package dev.enco.greatessentialsgui.objects;

import it.unimi.dsi.fastutil.objects.ObjectSet;

public record MenuContext(
        String title,
        int[] border,
        int rows,
        int maxPageItems,
        MenuItem extraItem,
        MenuItem emptyItem,
        ObjectSet<MenuItem> menuItems
) {}
