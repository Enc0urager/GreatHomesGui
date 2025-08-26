package dev.enco.greatessentialsgui.utils.colorizer.impl;

import dev.enco.greatessentialsgui.utils.colorizer.IColorizer;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;

public class MinimessageColorizer implements IColorizer {
    @Override
    public String colorize(String message) {
        var comp = MiniMessage.miniMessage().deserialize(message);
        return LegacyComponentSerializer.legacySection().serialize(comp);
    }
}
