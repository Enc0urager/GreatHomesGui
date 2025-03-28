package dev.enco.greatessentialsgui.utils;

import lombok.experimental.UtilityClass;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

@UtilityClass
public class Number {
    private final DecimalFormatSymbols symbols = new DecimalFormatSymbols(Locale.US);
    private final DecimalFormat decimalFormat = new DecimalFormat(Config.decimalFormat, symbols);

    public String format(double value) {
        return decimalFormat.format(value);
    }
}
