package dev.enco.greatessentialsgui.builder;

import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntList;
import lombok.experimental.UtilityClass;

@UtilityClass
public class SlotsParser {
    public int[] parse(String settings) {
        if (settings == null) return null;
        IntList result = new IntArrayList();
        var parts = settings.split(",");
        for (String part : parts) {
            if (part.contains("-")) {
                var range = part.split("-");
                int start = Integer.parseInt(range[0]);
                int end = Integer.parseInt(range[1]);
                for (int i = start; i <= end; i++) {
                    result.add(i);
                }
            } else {
                result.add(Integer.parseInt(part));
            }
        }
        return result.toIntArray();
    }
}
