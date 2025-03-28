package dev.enco.greatessentialsgui.actions;

import dev.enco.greatessentialsgui.utils.Colorizer;
import dev.enco.greatessentialsgui.utils.Logger;
import lombok.experimental.UtilityClass;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

@UtilityClass
public class ActionRegistry {
    private static final Pattern ACTION_PATTERN = Pattern.compile("\\[(\\S+)] ?(.*)");

    public Map<ActionType, List<String>> transform(List<String> settings) {
        Map<ActionType, List<String>> actions = new HashMap<>();
        settings.forEach(s -> {
            var matcher = ACTION_PATTERN.matcher(s);
            if (!matcher.matches()) {
                Logger.warn("Невалидное действие " + s + " проверьте конфиг!");
                return;
            }
            var type = ActionType.valueOf(matcher.group(1));
            if (type == null) {
                Logger.warn("Тип действия " + s + " не существует");
                return;
            }
            var context = matcher.group(2).trim();
            actions.putIfAbsent(type, new ArrayList<>());
            actions.get(type).add(Colorizer.colorize(context));
        });
        return actions;
    }
}
