package net.pixlies.core.moderation;

import dev.morphia.annotations.Entity;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Data
@AllArgsConstructor
@Entity
public class Punishment {

    private String ID;
    private String type;
    private String punisher;
    private long date;
    private String reason;
    private long until;

    public Punishment(Map<String, Object> mapped) {
        this.ID = (String)mapped.get("ID");
        this.type = (String) mapped.get("type");
        this.punisher = (String) mapped.get("punisher");
        this.date = (long) mapped.get("date");
        this.reason = (String) mapped.get("reason");
        this.until = (long) mapped.get("until");
    }

    public UUID getPunisherUUID() {
        return UUID.fromString(punisher);
    }

    public PunishmentType getTypeEnum() {
        return PunishmentType.valueOf(type);
    }

    public static Map<String, Punishment> getFromMongo(Map<String, Map<String, Object>> map) {
        Map<String, Punishment> walletMap = new HashMap<>();
        for (Map.Entry<String, Map<String, Object>> entry : map.entrySet())
            walletMap.put(entry.getKey(), new Punishment(entry.getValue()));
        return walletMap;
    }

    public Map<String, Object> mapForMongo() {
        Map<String, Object> map = new HashMap<>();
        map.put("ID", ID);
        map.put("type", type);
        map.put("punisher", punisher.toString());
        map.put("date", date);
        map.put("reason", reason);
        map.put("until", until);
        return map;
    }

    public static Map<String, Map<String, Object>> mapAllForMongo(Map<String, Punishment> punishmentMap) {
        Map<String, Map<String, Object>> map = new HashMap<>();
        for (Map.Entry<String, Punishment> entry : punishmentMap.entrySet())
            map.put(entry.getKey(), entry.getValue().mapForMongo());
        return map;
    }

    public boolean isPermanent() {
        return until == 0;
    }

    public boolean isExpired() {
        if (isPermanent()) return false;
        if (getUntil() == 0) return false;
        return System.currentTimeMillis() - getUntil() <= 0;
    }

}
