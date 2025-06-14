package src.vacuumCleanerRobot;

import java.awt.Point;
import java.util.Scanner;
import vacuumCleanerRobot.Game;

public class AppConsole {
    public static void main(String[] args) {
        try {
        	
            // Start the simulation in a thread
            Game game = new Game();
            Thread gameThread = new Thread(game);
            gameThread.start();
            
            // Give the simulation time to start
            Thread.sleep(1000);
            
            MainApp app = MainApp.getInstance();
            VacuumRobotBridge bridge = VacuumRobotBridge.getInstance();
            Scanner scanner = new Scanner(System.in);

            // Attach observers
            app.getScheduler().attach(new DashboardUI());
            app.getScheduler().attach(new NotificationManager());

            System.out.println("=== Integrated Vacuum Robot Control System ===");
            System.out.println("Simulation and control app are now connected!");

            while (true) {
                System.out.println("\n=== Robot Control ===");
                System.out.println("1. Start Auto Clean");
                System.out.println("2. Activate Pet Mode");
                System.out.println("3. Schedule Task");
                System.out.println("4. Check Firmware");
                System.out.println("5. Stop Cleaning");
                System.out.println("6. Robot Status");
                System.out.println("7. Manual Control");
                System.out.println("8. Exit");
                System.out.print("Select option: ");

                int choice = scanner.nextInt();
                scanner.nextLine();  // Consume newline

                switch (choice) {
                    case 1:
                        app.getRemoteControl().triggerCleaning("AUTO");
                        System.out.println("Auto cleaning started in simulation!");
                        break;
                    case 2:
                        app.getRemoteControl().triggerCleaning("PET");
                        System.out.println("Pet mode activated in simulation!");
                        break;
                    case 3:
                        System.out.print("Enter task configuration: ");
                        String config = scanner.nextLine();
                        app.getScheduler().scheduleTask(config);
                        break;
                    case 4:
                        app.getDeviceConnector().checkCompatibility();
                        break;
                    case 5:
                        bridge.stopCleaning();
                        System.out.println("Cleaning stopped!");
                        break;
                    case 6:
                        System.out.println("Current Status: " + bridge.getCleaningStats());
                        Point pos = bridge.getRobotPosition();
                        System.out.println("Robot Position: (" + pos.x + ", " + pos.y + ")");
                        break;
                    case 7:
                        System.out.println("Manual Control Mode");
                        System.out.print("Enter X speed (-5 to 5): ");
                        double speedX = scanner.nextDouble();
                        System.out.print("Enter Y speed (-5 to 5): ");
                        double speedY = scanner.nextDouble();
                        bridge.setRobotSpeed(speedX, speedY);
                        System.out.println("Manual speed set to: (" + speedX + ", " + speedY + ")");
                        break;
                    case 8:
                        scanner.close();
                        System.out.println("Shutting down integrated system...");
                        System.exit(0);
                        return;
                    default:
                        System.out.println("Invalid selection");
                }
            }
        } catch (Exception e) {
            System.err.println("Error starting integrated system: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
