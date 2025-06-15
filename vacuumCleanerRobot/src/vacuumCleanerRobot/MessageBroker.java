package vacuumCleanerRobot;

import java.util.*;
import java.util.concurrent.*;

public class MessageBroker {
    private static MessageBroker instance;
    private final Map<String, List<MessageHandler>> subscribers;
    private final BlockingQueue<Message> messageQueue;
    private final ExecutorService executorService;
    private volatile boolean running;
    
    private MessageBroker() {
        this.subscribers = new ConcurrentHashMap<>();
        this.messageQueue = new LinkedBlockingQueue<>();
        this.executorService = Executors.newFixedThreadPool(4);
        this.running = true;
        startMessageProcessing();
    }
    
    public static synchronized MessageBroker getInstance() {
        if (instance == null) {
            instance = new MessageBroker();
        }
        return instance;
    }
    
    public void subscribe(String messageType, MessageHandler handler) {
        subscribers.computeIfAbsent(messageType, k -> new ArrayList<>()).add(handler);
        System.out.println("[MessageBroker] " + handler.getHandlerName() + 
                          " subscribed to " + messageType);
    }
    
    public void unsubscribe(String messageType, MessageHandler handler) {
        List<MessageHandler> handlers = subscribers.get(messageType);
        if (handlers != null) {
            handlers.remove(handler);
        }
    }
    
    public void publish(Message message) {
        try {
            messageQueue.put(message);
            System.out.println("[MessageBroker] Published: " + message);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.err.println("Failed to publish message: " + e.getMessage());
        }
    }
    
    private void startMessageProcessing() {
        executorService.submit(() -> {
            while (running) {
                try {
                    Message message = messageQueue.take();
                    processMessage(message);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        });
    }
    
    private void processMessage(Message message) {
        List<MessageHandler> handlers = subscribers.get(message.getType());
        if (handlers != null) {
            for (MessageHandler handler : handlers) {
                executorService.submit(() -> {
                    try {
                        handler.handleMessage(message);
                    } catch (Exception e) {
                        System.err.println("Error handling message: " + e.getMessage());
                    }
                });
            }
        }
    }
    
    public void shutdown() {
        running = false;
        executorService.shutdown();
    }
}