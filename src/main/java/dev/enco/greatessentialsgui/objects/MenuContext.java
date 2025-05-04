package dev.enco.greatessentialsgui.objects;

import java.util.List;
import java.util.Set;

public record MenuContext(
        String title,
        List<Integer> border,
        int rows,
        int maxPageItems,
        MenuItem extraItem,
        Set<MenuItem> menuItems
) {}
