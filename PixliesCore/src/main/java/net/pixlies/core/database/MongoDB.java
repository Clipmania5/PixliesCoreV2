package net.pixlies.core.database;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import lombok.Getter;
import net.pixlies.core.Main;
import net.pixlies.core.entity.User;
import org.bson.Document;
import org.bukkit.Bukkit;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Getter
public class MongoDB {

    private static final Main instance = Main.getInstance();

    private MongoClient client;
    private MongoDatabase database;

    private MongoCollection<Document> userCollection;
    private final Map<UUID, User> userCache = new HashMap<>();

    public MongoDB init() {
        String uri = instance.getConfig().getString("database.uri");
        if (uri == null) {
            instance.getLogger().warning("Plugin can't start because MongoDB URI is missing.");
            Bukkit.getPluginManager().disablePlugin(instance);
            return this;
        }
        MongoClientURI clientURI = new MongoClientURI(uri);
        client = new MongoClient(clientURI);

        database = client.getDatabase(instance.getConfig().getString("database.database", "admin"));

        userCollection = database.getCollection(instance.getConfig().getString("database.users-collection", "users"));

        return this;
    }

}
