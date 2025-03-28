package dev.enco.greatessentialsgui.builder;
import lombok.experimental.UtilityClass;

import java.util.ArrayList;
import java.util.List;

@UtilityClass
public class SlotsParser {
    public List<Integer> parse(String settings) {
        List<Integer> result = new ArrayList<>();
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
