package vacuumCleanerRobot;

public class BaseMessage implements Message {
    private final String type;
    private final String payload;
    private final long timestamp;
    private final String sender;
    private final String receiver;
    
    public BaseMessage(String type, String payload, String sender, String receiver) {
        this.type = type;
        this.payload = payload;
        this.sender = sender;
        this.receiver = receiver;
        this.timestamp = System.currentTimeMillis();
    }
    
    @Override
    public String getType() { return type; }
    
    @Override
    public String getPayload() { return payload; }
    
    @Override
    public long getTimestamp() { return timestamp; }
    
    @Override
    public String getSender() { return sender; }
    
    @Override
    public String getReceiver() { return receiver; }
    
    @Override
    public String toString() {
        return String.format("Message[type=%s, from=%s, to=%s, payload=%s]", 
                           type, sender, receiver, payload);
    }
}