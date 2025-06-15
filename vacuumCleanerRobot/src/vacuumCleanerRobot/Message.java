package vacuumCleanerRobot;

public interface Message {
    String getType();
    String getPayload();
    long getTimestamp();
    String getSender();
    String getReceiver();
}