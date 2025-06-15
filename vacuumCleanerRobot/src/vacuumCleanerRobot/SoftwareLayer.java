package vacuumCleanerRobot;

public class SoftwareLayer implements MessageHandler {
    private final MessageBroker broker;
    private final CleaningModeFactory modeFactory;
    private String lastStatus = "Unknown";
    
    public SoftwareLayer() {
        this.broker = MessageBroker.getInstance();
        this.modeFactory = new CleaningModeFactory();
        subscribeToMessages();
    }
    
    private void subscribeToMessages() {
        broker.subscribe("HARDWARE_EVENT", this);
        broker.subscribe("HARDWARE_STATUS", this);
        broker.subscribe("SOFTWARE_COMMAND", this);
    }
    
    @Override
    public void handleMessage(Message message) {
        switch (message.getType()) {
            case "HARDWARE_EVENT":
                handleHardwareEvent(message.getPayload());
                break;
            case "HARDWARE_STATUS":
                handleStatusUpdate(message.getPayload());
                break;
            case "SOFTWARE_COMMAND":
                handleSoftwareCommand(message.getPayload());
                break;
        }
    }
    
    private void handleHardwareEvent(String event) {
        System.out.println("[SoftwareLayer] Hardware Event: " + event);
        publishMessage("SOFTWARE_EVENT", "Processed hardware event: " + event, "UI");
    }
    
    private void handleStatusUpdate(String status) {
        lastStatus = status;
        publishMessage("SOFTWARE_EVENT", "Status updated: " + status, "UI");
    }
    
    private void handleSoftwareCommand(String command) {
        String[] parts = command.split(":");
        String cmd = parts[0];
        
        switch (cmd) {
            case "START_CLEANING":
                if (parts.length >= 2) {
                    String mode = parts[1];
                    CleaningMode cleaningMode = modeFactory.createMode(mode);
                    cleaningMode.executeCleaning();
                    publishMessage("CLEANING_COMMAND", "START_CLEANING:" + mode, "HARDWARE");
                }
                break;
            case "STOP_CLEANING":
                publishMessage("CLEANING_COMMAND", "STOP_CLEANING", "HARDWARE");
                break;
            case "SET_SPEED":
                if (parts.length >= 3) {
                    publishMessage("MOVEMENT_COMMAND", "SET_SPEED:" + parts[1] + ":" + parts[2], "HARDWARE");
                }
                break;
            case "REQUEST_STATUS":
                publishMessage("STATUS_REQUEST", "GET_STATUS", "HARDWARE");
                break;
        }
    }
    
    public String getLastStatus() {
        return lastStatus;
    }
    
    private void publishMessage(String type, String payload, String receiver) {
        Message msg = new BaseMessage(type, payload, "SOFTWARE", receiver);
        broker.publish(msg);
    }
    
    @Override
    public String getHandlerName() {
        return "SoftwareLayer";
    }
}
