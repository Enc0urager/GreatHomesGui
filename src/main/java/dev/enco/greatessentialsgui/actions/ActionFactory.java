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
public class ActionFactory {
    private static final Pattern ACTION_PATTERN = Pattern.compile("\\[(\\S+)] ?(.*)");
    private static final ImmutableMap<Class<? extends Context>, Function<String, ? extends Context>> VALIDATORS = ImmutableMap.of(
            StringContext.class, StringContext::validate,
            TitleContext.class, TitleContext::validate,
            SoundContext.class, SoundContext::validate,
            GuiContext.class, GuiContext::validate
    );

    public ActionMap from(List<String> settings) {
        if (settings == null || settings.isEmpty()) return ActionMap.EMPTY;
        Map<ActionType, List<Context>> actions = new HashMap<>();
        for (var s : settings) {
            var matcher = ACTION_PATTERN.matcher(s);
            if (!matcher.matches()) {
                Logger.warn("Illegal action pattern" + s);
                continue;
            }
            var typeName = matcher.group(1);
            ActionType type;
            try {
                type = ActionType.valueOf(typeName);
            } catch (IllegalArgumentException e) {
                Logger.warn("Action type " + typeName + "doesn't exist");
                continue;
            }

            var contextStr = matcher.group(2).trim();
            List<Context> contexts = actions.computeIfAbsent(type, k -> new ArrayList<>());

            Function<String, ? extends Context> validator = VALIDATORS.get(type.getContextType());
            Context context = validator.apply(contextStr);
            if (context != null) contexts.add(context);
        }
        ActionType[] types = new ActionType[actions.size()];
        Context[][] contexts = new Context[actions.size()][];

        int i = 0;
        for (var entry : actions.entrySet()) {
            types[i] = entry.getKey();
            List<Context> contextList = entry.getValue();
            contexts[i] = contextList.toArray(new Context[0]);
            i++;
        }

        return new ActionMap(types, contexts);
    }
}
