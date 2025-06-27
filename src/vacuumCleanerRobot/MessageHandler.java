package vacuumCleanerRobot;

public interface MessageHandler {
    void handleMessage(Message message);
    String getHandlerName();
}
