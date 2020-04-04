package de.icevizion.aves.message;

public class Message {

    private final String message;
    private final int displayTime;
    private final boolean repeating;

    private transient int time;

    public Message(String message, int displayTime, boolean repeating) {
        this.message = message;
        this.displayTime = displayTime;
        this.repeating = repeating;
        this.time = displayTime;
    }

    public void decrease() {
        time--;
    }

    public boolean isRepeating() {
        return repeating;
    }

    public int getTime() {
        return time;
    }

    public String getMessage() {
        return message;
    }
}
