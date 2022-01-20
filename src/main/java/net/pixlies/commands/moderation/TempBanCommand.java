package net.pixlies.commands.moderation;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.CommandHelp;
import co.aikar.commands.annotation.*;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import net.pixlies.Main;
import net.pixlies.entity.User;
import net.pixlies.localization.Lang;
import net.pixlies.utils.TimeUnit;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.ocpsoft.prettytime.PrettyTime;

import java.util.Date;

@CommandAlias("tempban")
@CommandPermission("pixlies.moderation.tempban")
public class TempBanCommand extends BaseCommand {

    @CommandCompletion("@players")
    @Description("Temporarily bans player with the default reason")
    public static void onTempBan(CommandSender sender, String player, String duration, @Optional String reason) {
        boolean silent = false;
        long durationLong = TimeUnit.getDuration(duration);
        String banReason = Main.getInstance().getConfig().getString("moderation.defaultReason", "No reason given");
        if (reason != null && !reason.isEmpty()) {
            banReason = reason.replace("-s", "");
            if (reason.endsWith("-s") || reason.startsWith("-s"))
                silent = true;
        }

        String banMessage = Lang.BAN_MESSAGE.get(sender)
                .replace("%REASON%", banReason)
                .replace("%BAN_ID%", "§cRejoin to find.")
                .replace("%DURATION%", new PrettyTime().format(new Date(durationLong + System.currentTimeMillis())));

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
        user.tempBan(banReason, sender, durationLong, silent);
    }

    @Default
    @HelpCommand
    public static void onHelp(CommandSender sender, CommandHelp help) {
        help.showHelp();
    }

    public TempBanCommand() {}

}
