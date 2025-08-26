package dev.enco.greatessentialsgui.utils.colorizer;

import dev.enco.greatessentialsgui.utils.colorizer.impl.LegacyColorizer;
import dev.enco.greatessentialsgui.utils.colorizer.impl.MinimessageColorizer;
import lombok.experimental.UtilityClass;

import java.util.ArrayList;
import java.util.List;

@UtilityClass
public class Colorizer {
    private IColorizer colorizer;

    public void setColorizer(String type) {
        switch (type) {
            case "MINIMESSAGE" -> colorizer = new MinimessageColorizer();
            default -> colorizer = new LegacyColorizer();
        }
    }

    public String colorize(String message) {
        if (message == null || message.isEmpty()) return message;
        return colorizer.colorize(message);
    }

    public List<String> colorizeAll(List<String> list) {
        List<String> colored = new ArrayList<>();
        for (var str : list) colored.add(colorize(str));
        return colored;
    }
}
