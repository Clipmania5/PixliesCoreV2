package net.pixlies.commands.moderation;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.CommandHelp;
import co.aikar.commands.annotation.*;
import net.pixlies.Main;
import net.pixlies.entity.User;
import net.pixlies.localization.Lang;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;

@CommandAlias("mute")
@CommandPermission("pixlies.moderation.mute")
public class MuteCommand extends BaseCommand {

    @Default
    @CommandCompletion("@players")
    @Description("Mutes player with the default reason")
    public static void onBan(CommandSender sender, String player, @Optional String reason) {
        boolean silent = false;
        String muteReason = Main.getInstance().getConfig().getString("moderation.defaultReason", "No reason given");
        if (reason != null && !reason.isEmpty()) {
            muteReason = reason.replace("-s", "");
            if (reason.endsWith("-s") || reason.startsWith("-s"))
                silent = true;
        }

        OfflinePlayer targetOP = Bukkit.getOfflinePlayerIfCached(player);
        if (targetOP == null) {
            Lang.PLAYER_DOESNT_EXIST.send(sender);
            return;
        }

        User user = User.get(targetOP.getUniqueId());
        user.mute(muteReason, sender, silent);
    }

    @HelpCommand
    public static void onHelp(CommandSender sender, CommandHelp help) {
        help.showHelp();
    }

    public MuteCommand() {}

}
