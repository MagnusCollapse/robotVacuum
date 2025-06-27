package vacuumCleanerRobot;

import java.awt.Point;

public class HardwareAbstractionLayer implements MessageHandler {
    private final MessageBroker broker;
    private TestObject robotHardware;
    private String currentMode = "AUTO";
    private boolean isActive = false;
    
    public HardwareAbstractionLayer() {
        this.broker = MessageBroker.getInstance();
        subscribeToMessages();
    }
    
    private void subscribeToMessages() {
        broker.subscribe("HARDWARE_COMMAND", this);
        broker.subscribe("MOVEMENT_COMMAND", this);
        broker.subscribe("CLEANING_COMMAND", this);
        broker.subscribe("STATUS_REQUEST", this);
    }
    
    public void connectHardware(TestObject robotHardware) {
        this.robotHardware = robotHardware;
        publishMessage("HARDWARE_EVENT", "Hardware connected", "SOFTWARE");
    }
    
    @Override
    public void handleMessage(Message message) {
        String[] parts = message.getPayload().split(":");
        String command = parts[0];
        
        switch (message.getType()) {
            case "HARDWARE_COMMAND":
                handleHardwareCommand(command, parts);
                break;
            case "MOVEMENT_COMMAND":
                handleMovementCommand(command, parts);
                break;
            case "CLEANING_COMMAND":
                handleCleaningCommand(command, parts);
                break;
            case "STATUS_REQUEST":
                handleStatusRequest();
                break;
        }
    }
    
    private void handleHardwareCommand(String command, String[] parts) {
        switch (command) {
            case "START":
                isActive = true;
                publishMessage("HARDWARE_EVENT", "Robot started", "SOFTWARE");
                break;
            case "STOP":
                isActive = false;
                if (robotHardware != null) {
                    robotHardware.setSpeed(new Vector2D(0, 0));
                }
                publishMessage("HARDWARE_EVENT", "Robot stopped", "SOFTWARE");
                break;
            case "RESET":
                resetHardware();
                break;
        }
    }
    
    private void handleMovementCommand(String command, String[] parts) {
        if (!isActive || robotHardware == null) return;
        
        switch (command) {
            case "SET_SPEED":
                if (parts.length >= 3) {
                    double x = Double.parseDouble(parts[1]);
                    double y = Double.parseDouble(parts[2]);
                    robotHardware.setSpeed(new Vector2D(x, y));
                    publishMessage("HARDWARE_EVENT", "Speed set to " + x + "," + y, "SOFTWARE");
                }
                break;
            case "MOVE_TO":
                if (parts.length >= 3) {
                    int x = Integer.parseInt(parts[1]);
                    int y = Integer.parseInt(parts[2]);
                    robotHardware.setAddress(new Point(x, y));
                    publishMessage("HARDWARE_EVENT", "Moving to " + x + "," + y, "SOFTWARE");
                }
                break;
        }
    }
    
    private void handleCleaningCommand(String command, String[] parts) {
        switch (command) {
            case "START_CLEANING":
                if (parts.length >= 2) {
                    currentMode = parts[1];
                    isActive = true;
                    applyCleaningMode(currentMode);
                    publishMessage("HARDWARE_EVENT", "Cleaning started in " + currentMode + " mode", "SOFTWARE");
                }
                break;
            case "STOP_CLEANING":
                isActive = false;
                publishMessage("HARDWARE_EVENT", "Cleaning stopped", "SOFTWARE");
                break;
        }
    }
    
    private void handleStatusRequest() {
        if (robotHardware != null) {
            Point pos = robotHardware.getPosition();
            Vector2D speed = robotHardware.getSpeed();
            String status = String.format("Position:%d,%d|Speed:%.1f,%.1f|Mode:%s|Active:%s",
                                        pos.x, pos.y, speed.VectorX(), speed.VectorY(), currentMode, isActive);
            publishMessage("HARDWARE_STATUS", status, "SOFTWARE");
        }
    }
    
    private void applyCleaningMode(String mode) {
        if (robotHardware == null) return;
        
        if ("PET".equalsIgnoreCase(mode)) {
            robotHardware.setSpeed(new Vector2D(1.5, 1.0));
        } else {
            robotHardware.setSpeed(new Vector2D(2.2, 1.3));
        }
    }
    
    private void resetHardware() {
        isActive = false;
        currentMode = "AUTO";
        if (robotHardware != null) {
            robotHardware.setSpeed(new Vector2D(0, 0));
            robotHardware.setAddress(new Point(100, 100));
        }
        publishMessage("HARDWARE_EVENT", "Hardware reset", "SOFTWARE");
    }
    
    private void publishMessage(String type, String payload, String receiver) {
        Message msg = new BaseMessage(type, payload, "HARDWARE", receiver);
        broker.publish(msg);
    }
    
    @Override
    public String getHandlerName() {
        return "HardwareAbstractionLayer";
    }
}

