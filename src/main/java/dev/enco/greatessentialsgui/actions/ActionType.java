package dev.enco.greatessentialsgui.actions;

import dev.enco.greatessentialsgui.actions.impl.*;
import lombok.Getter;

@Getter
public enum ActionType {
    MESSAGE(new MessageAction()),
    SOUND(new SoundAction()),
    ACTIONBAR(new ActionBarAction()),
    TITLE(new TitleAction()),
    CONSOLE(new ConsoleAction()),
    CLOSE(new CloseAction()),
    NEXT(new NextPageAction()),
    PREV(new PrevPageAction()),
    UPDATE(new UpdateAction()),
    PLAYER(new PlayerAction());

    private final Action action;

    ActionType(Action action) {
        this.action = action;
    }
}
