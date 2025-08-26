package dev.enco.greatessentialsgui.actions;

import com.google.common.collect.ImmutableMap;
import dev.enco.greatessentialsgui.actions.context.*;
import dev.enco.greatessentialsgui.utils.logger.Logger;
import lombok.experimental.UtilityClass;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.regex.Pattern;

@UtilityClass
public class ActionRegistry {
    private static final Pattern ACTION_PATTERN = Pattern.compile("\\[(\\S+)] ?(.*)");
    private static final ImmutableMap<Class<? extends Context>, Function<String, ? extends Context>> VALIDATORS = ImmutableMap.of(
            StringContext.class, StringContext::validate,
            TitleContext.class, TitleContext::validate,
            SoundContext.class, SoundContext::validate,
            GuiContext.class, GuiContext::validate
    );

    public ImmutableMap<ActionType, List<Context>> transform(List<String> settings) {
        Map<ActionType, List<Context>> actions = new HashMap<>();
        for (var s : settings) {
            var matcher = ACTION_PATTERN.matcher(s);
            if (!matcher.matches()) {
                Logger.warn("Illegal action pattern " + s);
                continue;
            }
            var typeName = matcher.group(1);
            ActionType type;
            try {
                type = ActionType.valueOf(typeName);
            } catch (IllegalArgumentException e) {
                Logger.warn("ActionType " + typeName + "doesn't exist");
                continue;
            }

            var contextStr = matcher.group(2).trim();
            actions.putIfAbsent(type, new ArrayList<>());

            Function<String, ? extends Context> validator = VALIDATORS.get(type.getContextType());
            Context context = validator.apply(contextStr);
            if (context != null) {
                actions.get(type).add(context);
            }
        }
        return ImmutableMap.copyOf(actions);
    }
}
