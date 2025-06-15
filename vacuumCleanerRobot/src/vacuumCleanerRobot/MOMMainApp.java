package vacuumCleanerRobot;


public class MOMMainApp {
    private static MOMMainApp instance;
    private UserProfileManager profileManager;
    private MOMRemoteControlHandler remoteControl;
    private CleaningScheduler scheduler;
    private DeviceConnector deviceConnector;
    private HardwareAbstractionLayer hardwareLayer;
    private MessageBroker broker;

    private MOMMainApp() {

        broker = MessageBroker.getInstance();
        hardwareLayer = new HardwareAbstractionLayer();
        
        profileManager = new UserProfileManager();
        remoteControl = new MOMRemoteControlHandler();
        scheduler = new CleaningScheduler();
        deviceConnector = new DeviceConnector();
        
        System.out.println("[MOMMainApp] Initialized with Message-Oriented Middleware");
    }

    public static MOMMainApp getInstance() {
        if (instance == null) {
            instance = new MOMMainApp();
        }
        return instance;
    }
    
    public void connectHardware(TestObject robotHardware) {
        hardwareLayer.connectHardware(robotHardware);
    }

    public UserProfileManager getProfileManager() { return profileManager; }
    public MOMRemoteControlHandler getRemoteControl() { return remoteControl; }
    public CleaningScheduler getScheduler() { return scheduler; }
    public DeviceConnector getDeviceConnector() { return deviceConnector; }
    public HardwareAbstractionLayer getHardwareLayer() { return hardwareLayer; }
    
    public void shutdown() {
        broker.shutdown();
    }
}