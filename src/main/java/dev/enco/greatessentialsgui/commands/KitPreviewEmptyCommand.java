package dev.enco.greatessentialsgui.commands;

import com.earth2me.essentials.Essentials;
import dev.enco.greatessentialsgui.Main;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class KitPreviewEmptyCommand extends Command {
    private final Essentials ess = Main.getInstance().getEss();

    public KitPreviewEmptyCommand(String name) {
        super(name);
    }

    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull String label, String[] args) {
        return true;
    }

    @Override
    public @NotNull List<String> tabComplete(@NotNull CommandSender sender, @NotNull String alias, String[] args) {
        return new ArrayList<>(ess.getKits().getKitKeys());
    }
}
