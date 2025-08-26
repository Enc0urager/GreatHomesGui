package dev.enco.greatessentialsgui.builder;


import it.unimi.dsi.fastutil.ints.IntArraySet;
import it.unimi.dsi.fastutil.ints.IntSet;
import lombok.experimental.UtilityClass;

@UtilityClass
public class SlotsParser {
    public IntSet parse(String settings) {
        IntSet result = new IntArraySet();
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
        return result;
    }
}
