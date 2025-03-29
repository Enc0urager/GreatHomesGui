package dev.enco.greatessentialsgui.objects;

import java.util.List;
import java.util.Set;

public record WarpsGui(
        String title,
        List<Integer> border,
        int rows,
        int maxPageItems,
        MenuItem warpItem,
        Set<MenuItem> menuItems
) {}
