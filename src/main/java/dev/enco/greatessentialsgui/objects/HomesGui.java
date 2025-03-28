package dev.enco.greatessentialsgui.objects;

import java.util.List;
import java.util.Set;

public record HomesGui(
        String title,
        List<Integer> border,
        int rows,
        int maxPageItems,
        MenuItem homeItem,
        Set<MenuItem> menuItems
) {}
