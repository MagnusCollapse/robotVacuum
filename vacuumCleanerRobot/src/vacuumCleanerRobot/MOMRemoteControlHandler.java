
package vacuumCleanerRobot;

public class MOMRemoteControlHandler implements MessageHandler {
    private final MessageBroker broker;
    private final SoftwareLayer softwareLayer;
    
    public MOMRemoteControlHandler() {
        this.broker = MessageBroker.getInstance();
        this.softwareLayer = new SoftwareLayer();
        subscribeToMessages();
    }
    
    private void subscribeToMessages() {
        broker.subscribe("SOFTWARE_EVENT", this);
    }
    
    public void triggerCleaning(String modeType) {
        publishMessage("SOFTWARE_COMMAND", "START_CLEANING:" + modeType, "SOFTWARE");
        System.out.println("[RemoteControl] Cleaning command sent: " + modeType);
    }
    
    public void stopCleaning() {
        publishMessage("SOFTWARE_COMMAND", "STOP_CLEANING", "SOFTWARE");
        System.out.println("[RemoteControl] Stop cleaning command sent");
    }
    
    public void setRobotSpeed(double x, double y) {
        publishMessage("SOFTWARE_COMMAND", "SET_SPEED:" + x + ":" + y, "SOFTWARE");
        System.out.println("[RemoteControl] Speed command sent: " + x + ", " + y);
    }
    
    public String getRobotStatus() {
        publishMessage("SOFTWARE_COMMAND", "REQUEST_STATUS", "SOFTWARE");
        // In a real implementation this would wait for response or use callback
        return softwareLayer.getLastStatus();
    }
    
    @Override
    public void handleMessage(Message message) {
        System.out.println("[RemoteControl] Received event: " + message.getPayload());
    }
    
    private void publishMessage(String type, String payload, String receiver) {
        Message msg = new BaseMessage(type, payload, "REMOTE_CONTROL", receiver);
        broker.publish(msg);
    }
    
    @Override
    public String getHandlerName() {
        return "MOMRemoteControlHandler";
    }
}