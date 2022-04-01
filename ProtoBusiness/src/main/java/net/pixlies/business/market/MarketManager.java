package net.pixlies.business.market;

import lombok.Getter;
import net.pixlies.business.ProtoBusiness;
import net.pixlies.business.market.orders.Order;
import net.pixlies.business.market.orders.OrderBook;
import net.pixlies.business.market.orders.OrderItem;
import net.pixlies.core.entity.user.User;
import org.bukkit.entity.Player;

import java.util.*;

/**
 * Market manager
 * @author vPrototype_
 */
public class MarketManager {

    private static final ProtoBusiness instance = ProtoBusiness.getInstance();

    @Getter private final Map<String, OrderBook> books = new HashMap<>(); // ID, OrderBook

    public MarketManager() {
        loadAll();
    }

    public void backupAll() {
        for (OrderBook book : books.values()) {
            book.backup();
        }
    }

    public void loadAll() {
        if (instance.getMongoManager().getDatastore().find(OrderBook.class).iterator().toList().isEmpty()) {
            initializeBooks();
        } else {
            for (OrderBook book : instance.getMongoManager().getDatastore().find(OrderBook.class).iterator().toList()) {
                if (book.getBookId() != null) {
                    books.put(book.getBookId(), book);
                }
            }
        }
    }

    public void initializeBooks() {
        for (OrderItem item : OrderItem.values()) {
            OrderBook book = new OrderBook(item);
            books.put(book.getBookId(), book);
        }
    }

    public void resetBooks() {
        // Reset stats.yml
        instance.getStats().set("market.buyOrders", 0);
        instance.getStats().set("market.sellOrders", 0);
        instance.getStats().set("market.moneySpent", 0);
        instance.getStats().set("market.moneyGained", 0);

        // Clear all orders
        for (OrderBook book : books.values()) {
            book.getBuyOrders().clear();
            book.getSellOrders().clear();
            book.save();
        }
    }

    public void resetPlayer(Player player) {
        UUID uuid = player.getUniqueId();
        User user = User.get(uuid);

        // Reset player stats
        user.getStats().setBuyOrdersMade(0);
        user.getStats().setSellOrdersMade(0);
        user.getStats().setMoneySpent(0);
        user.getStats().setMoneyGained(0);

        // Clear orders of that player
        for (OrderBook book : books.values()) {
            book.getBuyOrders().removeIf(order -> order.getPlayerUUID() == uuid);
            book.getSellOrders().removeIf(order -> order.getPlayerUUID() == uuid);
            book.save();
        }

        // TODO reset money
    }

    public List<Order> getPlayerBuyOrders(UUID uuid) {
        List<Order> list = new LinkedList<>();
        for (OrderBook book : books.values()) {
            if (book.getBuyOrders() != null) {
                for (Order order : book.getBuyOrders()) {
                    if (order.getPlayerUUID() == uuid) list.add(order);
                }
            }
        }
        return list;
    }

    public List<Order> getPlayerSellOrders(UUID uuid) {
        List<Order> list = new LinkedList<>();
        for (OrderBook book : books.values()) {
            if (book.getSellOrders() != null) {
                for (Order order : book.getSellOrders()) {
                    if (order.getPlayerUUID() == uuid) list.add(order);
                }
            }
        }
        return list;
    }

}
