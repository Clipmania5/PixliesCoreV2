package net.pixlies.commands.moderation;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.CommandHelp;
import co.aikar.commands.annotation.*;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import net.pixlies.Main;
import net.pixlies.entity.User;
import net.pixlies.localization.Lang;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;

@CommandAlias("ban")
@CommandPermission("pixlies.moderation.ban")
public class BanCommand extends BaseCommand {

    @CommandCompletion("@players")
    @Description("Bans player with the default reason")
    public static void onBan(CommandSender sender, String player, @Optional String reason) {
        boolean silent = false;
        String banReason = Main.getInstance().getConfig().getString("moderation.defaultReason", "No reason given");
        if (reason != null && !reason.isEmpty()) {
            banReason = reason.replace("-s", "");
            if (reason.endsWith("-s") || reason.startsWith("-s"))
                silent = true;
        }

        String banMessage = Lang.BAN_MESSAGE.get(sender)
                .replace("%REASON%", banReason)
                .replace("%BAN_ID%", "§cRejoin to find.")
                .replace("%DURATION%", "§4§lPERMANENT!");

        OfflinePlayer targetOP = Bukkit.getOfflinePlayerIfCached(player);
        if (targetOP == null || targetOP.getPlayer() == null) {
            Lang.PLAYER_DOESNT_EXIST.send(sender);
            return;
        }
        if (targetOP.isOnline()) {
            Component banKickMessage = LegacyComponentSerializer.legacyAmpersand().deserialize(banMessage);
            targetOP.getPlayer().kick(banKickMessage);
        }
        User user = User.get(targetOP.getUniqueId());
        user.ban(banReason, sender, silent);
    }

    @Default
    @HelpCommand
    public static void onHelp(CommandSender sender, CommandHelp help) {
        help.showHelp();
    }

    public BanCommand() {}

}
