package dev.enco.greatessentialsgui.actions;

import dev.enco.greatessentialsgui.actions.context.*;
import dev.enco.greatessentialsgui.actions.impl.*;
import lombok.Getter;

@Getter
public enum ActionType {
    MESSAGE(new MessageAction(), StringContext.class),
    SOUND(new SoundAction(), SoundContext.class),
    ACTIONBAR(new ActionBarAction(), StringContext.class),
    TITLE(new TitleAction(), TitleContext.class),
    CONSOLE(new ConsoleAction(), StringContext.class),
    CLOSE(new CloseAction(), GuiContext.class),
    NEXT(new NextPageAction(), GuiContext.class),
    PREV(new PrevPageAction(), GuiContext.class),
    UPDATE(new UpdateAction(), GuiContext.class),
    PLAYER(new PlayerAction(), StringContext.class),
    REOPEN(new ReopenAction(), GuiContext.class);

    private final Action<?> action;
    private final Class<? extends Context> contextType;

    ActionType(Action<?> action, Class<? extends Context> contextType) {
        this.action = action;
        this.contextType = contextType;
    }

    public <C extends Context> Action<C> getAction() {
        return (Action<C>) action;
    }
}
