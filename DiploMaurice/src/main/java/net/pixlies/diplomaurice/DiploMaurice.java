package net.pixlies.diplomaurice;

import net.pixlies.diplomaurice.listeners.ListenerLoader;
import net.pixlies.core.modules.Module;
import org.bukkit.plugin.java.JavaPlugin;
import org.javacord.api.DiscordApi;
import org.javacord.api.DiscordApiBuilder;
import org.javacord.api.entity.activity.ActivityType;
import org.javacord.api.entity.user.UserStatus;
import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class DiploMaurice extends JavaPlugin implements Module {
    private static DiploMaurice instance;
    private String token;

    @Override
    public void onEnable() {
        instance = this;
        init();
        new Thread(this::start);
    }

    @Override
    public void onDisable() {
        instance = null;
    }

    public void start() {
        DiscordApi api = new DiscordApiBuilder()
                .setToken(token)
                .login().join();

        api.updateActivity(ActivityType.CUSTOM, "doing diplomacy");
        api.updateStatus(UserStatus.ONLINE);

        // System.out.println("INFO | Bot Invite Link: " + api.createBotInvite());

        new ListenerLoader(api).loadAll();
    }
    
    private void init() {
        try {
            File tokenFile = new File("TOKEN");
            BufferedReader br = new BufferedReader(new FileReader(tokenFile));
            token = br.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @NotNull
    @Override
    public JavaPlugin getJavaPlugin() {
        return this;
    }

}
