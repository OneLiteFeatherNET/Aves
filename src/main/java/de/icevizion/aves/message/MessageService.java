package de.icevizion.aves.message;

import de.icevizion.aves.util.PlayerUtil;
import org.bukkit.entity.Player;

import java.util.ArrayDeque;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

public class MessageService implements Runnable {

    private final Set<Player> players;
    private final ArrayDeque<Message> messages;
    private Message currentMessage;

    public MessageService() {
        this.players = new CopyOnWriteArraySet<>();
        this.messages = new ArrayDeque<>();
    }

    @Override
    public synchronized void run() {
        if (currentMessage != null) {
            if (currentMessage.getTime() > 0) {
                currentMessage.decrease();
                for (Player player : players) {
                    PlayerUtil.sendActionBar(player, currentMessage.getMessage());
                }
            } else {
                if (currentMessage.isRepeating()) {
                    messages.addLast(currentMessage);
                }
                currentMessage = null;
            }
        } else {
            currentMessage = getNextMessage();
        }
    }

    private Message getNextMessage() {
        return messages.pollFirst();
    }

    public synchronized void addMessage(Message message) {
        this.messages.add(message);
    }

    public synchronized void removeMessage(Message message) {
        this.messages.remove(message);
    }

    public void addPlayer(Player paramPlayer) {
        players.add(paramPlayer);
    }

    public void removePlayer(Player paramPlayer) {
        players.remove(paramPlayer);
    }

    public Set<Player> getPlayers() {
        return players;
    }

    public Queue<Message> getMessages() {
        return messages;
    }
}