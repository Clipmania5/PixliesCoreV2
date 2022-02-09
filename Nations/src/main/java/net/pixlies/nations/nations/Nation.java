package net.pixlies.nations.nations;

import lombok.AllArgsConstructor;
import lombok.Data;
import net.pixlies.nations.Nations;
import net.pixlies.nations.nations.customization.GovernmentType;
import net.pixlies.nations.nations.customization.Ideology;
import net.pixlies.nations.nations.customization.Religion;
import net.pixlies.nations.nations.ranks.NationRank;
import org.bson.Document;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Data
@AllArgsConstructor
public class Nation {

    private static final Nations instance = Nations.getInstance();
    private static final NationManager nationManager = new NationManager();

    // INFO
    private String id;
    private String name;
    private String description;
    private UUID leaderUUID;
    private long created;

    // DATA
    private double politicalPower;
    private double money;

    // CUSTOMIZATION
    private GovernmentType govType;
    private Ideology ideology;
    private Religion religion;
    private List<Integer> constitutionValues;

    // STATES
    private List<String> stateIds;

    // RANKS
    private Map<String, Map<String, Object>> ranks;

    // MEMBERS
    private List<String> memberUUIDs;

    public Nation create() {
        ranks.put("leader", NationRank.leader().toMap());
        ranks.put("admin", NationRank.admin().toMap());
        ranks.put("member", NationRank.member().toMap());
        ranks.put("newbie", NationRank.newbie().toMap());

        save();
        return this;
    }

    public void save() {
        instance.getNationManager().getNations().put(id, this);
    }

    public void backup() {
        Document nation = new Document("id", id);
        Document found = instance.getMongoManager().getNationCollection().find(nation).first();

        // INFO
        nation.append("name", name);
        nation.append("description", description);
        nation.append("leaderUUID", leaderUUID);
        nation.append("created", created);

        // DATA
        nation.append("politicalPower", politicalPower);
        nation.append("money", money);

        // CUSTOMIZATION
        nation.append("ideology", ideology.toString());
        nation.append("govType", govType.toString());
        nation.append("religion", religion.toString());
        nation.append("constitutionValues", constitutionValues);

        // STATES
        nation.append("stateIds", stateIds);

        // RANKS
        nation.append("ranks", ranks);

        // MEMBERS
        nation.append("memberUUIDs", memberUUIDs);

        if (found != null) {
            instance.getMongoManager().getNationCollection().replaceOne(found, nation);
        } else {
            instance.getMongoManager().getNationCollection().insertOne(nation);
        }

    }

    public static Nation getFromId(String id) {
        return nationManager.getNations().get(id);
    }

    private void editConstitution(byte law, int option) {
        constitutionValues.set(law, option);
    }

    
}
