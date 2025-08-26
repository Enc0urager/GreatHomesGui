package dev.enco.greatessentialsgui.actions.context;

import dev.enco.greatessentialsgui.utils.colorizer.Colorizer;

public record StringContext(
        String string
) implements Context {
    public static StringContext validate(String s) {
        return new StringContext(Colorizer.colorize(s));
    }
}
